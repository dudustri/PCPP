package mobilepayment;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

public class Account extends AbstractBehavior<Account.AccountCommand> {

    /* --- Messages ------------------------------------- */
    public interface AccountCommand {
    } // Marker interface

    public static final class Start implements AccountCommand {
    }

    /**
     * Deposit command
     * handle deopsit operation
     */
    public static final class Deposit implements AccountCommand {
        public final double amount;

        public Deposit(double amount) {
            this.amount = amount;
        }
    }

    /**
     * Withdraw command
     * handle withdraw operation
     */
    public static final class Withdraw implements AccountCommand {
        public final double amount;

        public Withdraw(double amount) {
            this.amount = amount;
        }
    }

    /**
     * Message for printing the balance
     */
    public static final class PrintBalance implements AccountCommand {
    }

    /* --- State ---------------------------------------- */
    public double balance;
    public final String accountId;

    /* --- Constructor ---------------------------------- */
    // Feel free to extend the contructor at your convenience

    /**
     * Create a new account
     * 
     * @param context        Actor context
     * @param accountId      Account id
     * @param initialBalance Initial balance
     */
    private Account(ActorContext<AccountCommand> context, String accountId, double initialBalance) {
        super(context); // runs the whole envrionment - actor system
        this.accountId = accountId;
        this.balance = initialBalance;
        context.getLog().info(String.format("Account Actor started-> id:%s balance:%.2f", this.accountId, this.balance),
                context.getSelf().path().name());
    }

    /* --- Actor initial state -------------------------- */
    // To be Implemented

    /**
     * Behavior factory - create a new account with id and initial balance
     * instantiate the actor with the initial state
     * 
     * @param accountId      Account id
     * @param initialBalance Initial balance
     * @return Behavior
     */
    public static Behavior<AccountCommand> create(String accountId, double initialBalance) {
        return Behaviors.setup(context -> new Account(context, accountId, initialBalance));
    }

    /* --- Message handling ----------------------------- */
    /**
     * Message handler - creates and receives messages
     * handles different types of messages and returns the new behavior
     * 
     * @return Receive
     */
    @Override
    public Receive<AccountCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(Start.class, this::onStart)
                .onMessage(Deposit.class, this::onDeposit) // match the message type and call the handler
                .onMessage(Withdraw.class, this::onWithdraw) // match the message type and call the handler
                .onMessage(PrintBalance.class, this::onPrintBalance)
                .build();
    }

    /* --- Handlers ------------------------------------- */

    private Behavior<AccountCommand> onStart(Start command) {
        return this;
    }

    private Behavior<AccountCommand> onDeposit(Deposit command) {
        getContext().getLog().info("Account Id: {} Deposit received: {}", this.accountId, command.amount); // for
                                                                                                           // logging
                                                                                                           // and
                                                                                                           // testing
        balance += command.amount;
        // Additional logic or logging
        return this;
    }

    private Behavior<AccountCommand> onWithdraw(Withdraw command) {
        if (command.amount > balance) {
            getContext().getLog().error("Insufficient funds for withdrawal: {} id: {}", command.amount, this.accountId);
            // send failure message to sender
            // optionally, send failure message to self
        } else {
            getContext().getLog().info("Account Id: {} Withdraw: {}", this.accountId, command.amount);
            balance -= command.amount;
            // Log or acknowledge the successful withdrawal
        }
        return this;
    }

    private Behavior<AccountCommand> onPrintBalance(PrintBalance command) {
        getContext().getLog().info("Current balance in account {}: {}", accountId, balance);
        return this;
    }
}
