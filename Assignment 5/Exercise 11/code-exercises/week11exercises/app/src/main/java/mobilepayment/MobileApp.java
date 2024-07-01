package mobilepayment;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

import java.util.Random;

public class MobileApp extends AbstractBehavior<MobileApp.MobileAppCommand> {

  /* --- Messages ------------------------------------- */
  public interface MobileAppCommand {}

  /**
   * Start command
   * Message for starting the mobile app
   */
  public static final class Start implements MobileAppCommand {
    // Empty for now, may be extended later with start-up configurations
  }

  /***
   * Make payments
   * Message for doing 100 payments for account a to account b with random amounts
   */
  // Message to initiate payment process
  public static final class MakePayments implements MobileAppCommand {
    public final ActorRef<Bank.BankCommand> bank;
    public final ActorRef<Account.AccountCommand> fromAccountId;
    public final ActorRef<Account.AccountCommand> toAccountId;
    public final boolean multipleTransactions;
    public final double amount;

    public MakePayments(ActorRef<Bank.BankCommand> bank, ActorRef<Account.AccountCommand> fromAccountId, ActorRef<Account.AccountCommand> toAccountId, boolean multipleTransactions, double amount) {
      this.bank = bank;
      this.fromAccountId = fromAccountId;
      this.toAccountId = toAccountId;
      this.multipleTransactions = multipleTransactions;
      this.amount = amount;
    }
  }

  /* --- State ---------------------------------------- */
  private static int mobileAppCount = 0;
  private final String mobileAppId;

  /* --- Constructor ---------------------------------- */
  private final Random random = new Random(); // Random number generator for amounts

  private MobileApp(ActorContext<MobileAppCommand> context) {
    super(context);
    mobileAppCount++;
    this.mobileAppId = "mobile pay " + mobileAppCount;
    context.getLog().info("MobileApp {} is now started and ready to initiate payments!",
        context.getSelf().path().name());
  }

  /* --- Actor initial state -------------------------- */
  public static Behavior<MobileApp.MobileAppCommand> create() {
    return Behaviors.setup(context -> new MobileApp(context));
    // You may extend the constructor if necessary
  }

  /* --- Message handling ----------------------------- */

  /**
   * Message handler for the mobile app handle the three above messages
   * @return
   */
  @Override
  public Receive<MobileAppCommand> createReceive() {
    return newReceiveBuilder()
            .onMessage(Start.class, this::onStart)
            .onMessage(MakePayments.class, this::onMakePayments)
            .build();
  }

  /* --- Handlers ------------------------------------- */

  /**
   * onStart handler for the mobile app
   * TODO: SH - not sure what to do here
   * @param msg
   * @return
   */
  private Behavior<MobileAppCommand> onStart(Start msg) {
    return this;
  }


  /**
   * onMakePayments handler for the mobile app
   * makes 100 payments with random amounts and sends them to the bank
   * @param command
   * @return
   */
  // Handler for the MakePayments message
  private Behavior<MobileAppCommand> onMakePayments(MakePayments command) {
    if(command.multipleTransactions) {
    // Execute 100 payments with random amounts
    for (int i = 0; i < 100; i++) {
      double amount = random.nextDouble() * 10.0; // Random amount between 0 and 10
      getContext().getLog().info(this.mobileAppId + " contacting bank - bank id: " + command.bank.path().name());
      command.bank.tell(new Bank.Transaction(command.fromAccountId, command.toAccountId, amount));
    }
  }
  else {
    getContext().getLog().info(this.mobileAppId + " contacting bank - bank id: " + command.bank.path().name());
    command.bank.tell(new Bank.Transaction(command.fromAccountId, command.toAccountId, command.amount));
  }
    return this;
  }
}