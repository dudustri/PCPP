package mobilepayment;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;
import java.util.Map;
import java.util.TreeMap;

public class Guardian extends AbstractBehavior<Guardian.GuardianCommand> {

  /* --- Messages ------------------------------------- */
  public interface GuardianCommand {
  }

  public static final class KickOff implements GuardianCommand {
  }

  public static final class MakeTransaction implements GuardianCommand {
    public final String mobileAppId;
    public final String bankId;
    public final String toAccountId;
    public final String fromAccountId;
    public final boolean multipleRandomTransactions;
    public final double amount;

    MakeTransaction(String mobileAppId, String bankId, String toAccountId, String fromAccount,
        boolean multipleRandomTransactions, double amount) {
      this.mobileAppId = mobileAppId;
      this.bankId = bankId;
      this.toAccountId = toAccountId;
      this.fromAccountId = fromAccount;
      this.multipleRandomTransactions = multipleRandomTransactions;
      this.amount = amount;
    }
  }

  public static final class GetBalance implements GuardianCommand {
    public final String account;

    GetBalance(String account) {
      this.account = account;
    }
  }

  // Feel free to add message types at your convenience

  /* --- State ---------------------------------------- */
  private static final double initialBalance = 1000;
  private static Map<String, ActorRef> actors;

  /* --- Constructor ---------------------------------- */
  private Guardian(ActorContext<GuardianCommand> context) {
    super(context);
  }

  /* --- Actor initial state -------------------------- */
  public static Behavior<Guardian.GuardianCommand> create() {
    return Behaviors.setup(Guardian::new);
  }

  /* --- Message handling ----------------------------- */
  @Override
  public Receive<GuardianCommand> createReceive() {
    return newReceiveBuilder().onMessage(KickOff.class, this::onKickOff)
        .onMessage(MakeTransaction.class, this::onMakeTransaction).onMessage(GetBalance.class, this::onGetBalance)
        .build();
  }

  /* --- Handlers ------------------------------------- */
  private Behavior<GuardianCommand> onKickOff(KickOff msg) {

    this.actors = new TreeMap<String, ActorRef>();
    // spawn 2 mobile pay actors
    ActorRef<MobileApp.MobileAppCommand> mobilepay = getContext().spawn(MobileApp.create(), "mb1");
    mobilepay.tell(new MobileApp.Start());
    ActorRef<MobileApp.MobileAppCommand> mobilepay2 = getContext().spawn(MobileApp.create(), "mb2");
    mobilepay2.tell(new MobileApp.Start());

    this.actors.put("mb1", mobilepay);
    this.actors.put("mb2", mobilepay2);

    // spawning 2 bank actors
    ActorRef<Bank.BankCommand> bank1 = getContext().spawn(Bank.create("1"), "b1");
    bank1.tell(new Bank.Start());
    ActorRef<Bank.BankCommand> bank2 = getContext().spawn(Bank.create("2"), "b2");
    bank2.tell(new Bank.Start());

    this.actors.put("b1", bank1);
    this.actors.put("b2", bank2);

    // spawning 2 account actors
    ActorRef<Account.AccountCommand> account1 = getContext().spawn(Account.create("account_1", initialBalance), "a1");
    account1.tell(new Account.Start());
    ActorRef<Account.AccountCommand> account2 = getContext().spawn(Account.create("account_2", initialBalance), "a2");
    account2.tell(new Account.Start());

    this.actors.put("a1", account1);
    this.actors.put("a2", account2);

    return this;
  }

  private Behavior<GuardianCommand> onMakeTransaction(MakeTransaction msg) {

    this.actors.get(msg.mobileAppId)
        .tell(new MobileApp.MakePayments(this.actors.get(msg.bankId), this.actors.get(msg.toAccountId),
            this.actors.get(msg.fromAccountId), msg.multipleRandomTransactions, msg.amount));
    return this;
  }

  private Behavior<GuardianCommand> onGetBalance(GetBalance msg) {

    this.actors.get(msg.account).tell(new Account.PrintBalance());
    return this;

  }
}