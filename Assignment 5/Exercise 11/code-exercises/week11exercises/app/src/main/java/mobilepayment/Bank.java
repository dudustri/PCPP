package mobilepayment;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

public class Bank extends AbstractBehavior<Bank.BankCommand> {

    /* --- Messages ------------------------------------- */
    public interface BankCommand {
    }
    // Feel free to add message types at your convenience

    public static final class Start implements BankCommand {
    }

    public static final class Transaction implements BankCommand {
        public final ActorRef<Account.AccountCommand> acc1;
        public final ActorRef<Account.AccountCommand> acc2;
        public final double amount;

        public Transaction(ActorRef<Account.AccountCommand> acc1, ActorRef<Account.AccountCommand> acc2, double amount) {
            this.acc1 = acc1;
            this.acc2 = acc2;
            this.amount = amount;
        }
    }

    /* --- State ---------------------------------------- */
    private final String bankId;

    /* --- Constructor ---------------------------------- */
    // Feel free to extend the contructor at your convenience
    private Bank(ActorContext<BankCommand> context, String bankId) {
        super(context);
        this.bankId = bankId;
        context.getLog().info(String.format("Bank Actor started-> id:%s", this.bankId),
                context.getSelf().path().name());
    }

    /* --- Actor initial state -------------------------- */
    //a method for creating the bank
    public static Behavior<BankCommand> create(String bankId) {
        return Behaviors.setup(context -> new Bank(context, bankId));
        
    }

    /* --- Message handling ----------------------------- */
    @Override
    public Receive<BankCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(Start.class, this::onStart)
                .onMessage(Transaction.class, this::onTransaction)
                .build();
    }

    /* --- Handlers ------------------------------------- */
    private Behavior<BankCommand> onStart(Start command) {
        return this;
    }

    private Behavior<BankCommand> onTransaction(Transaction command) {
        getContext().getLog().info("Bank {} processing request...", this.bankId);
        command.acc1.tell(new Account.Withdraw(command.amount));
        command.acc2.tell(new Account.Deposit(command.amount));
        return this;
    }
}
