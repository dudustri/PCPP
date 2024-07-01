# Exercises Week 11

## 11.1

### 11.1.1

The Guardian actor in our implementation manages the creation of the mobile payment system using the Akka framework.
It has no explicit state and, when it receives a KickOff message, it spawns a MobileApp actor named
"mobile_pay_system" and sends a Start message to it. The KickOff is a class message that implements
the GuardianCommand. In a higher level, the Main.java class instantiate a new ActorSystem and starts
the Guardian actor within it. It triggers the initialization of the mobile payment system (spawning it indirectly)
since it is sending a KickOff message to the new Guardian ("tell").

```java
guardian.tell(new Guardian.KickOff())
```

### 11.1.2

**General:**
The Account actor class is designed with the principles of the Akka actor model, emphasizing message-driven
interactions and encapsulation. The AccountCommand marker interface provides a unified type for all
account-related messages, ensuring type safety in the message handling.

**Messages:** The messages that we provide are Deposit and Withdraw which are immutable, carrying the necessary 
information for the actor to modify its state—the account balance—safely within its context.

**Initialization** The actor is initialized through a private constructor that sets the accountId and initialBalance, 
reflecting the immutability of an account identifier and the dynamic nature of an account balance. An
ActorContext is also provided, equipping the actor with the capabilities to interact within the Akka system.
A static create method serves as a factory, abstracting the instantiation process and returning the initial behavior
setup of the actor.

**Message Handling** is defined by the `createReceive` method, mapping types to their corresponding handler methods.
The onDeposit handler updates the balance while logging the transaction for traceability.
The onWithdraw handler incorporates validation to ensure sufficient funds are available, maintaining the
integrity of the account's state. The actor's design allows for comprehensive error handling strategies, like
responding to the sender with failure messages, although specifics are not provided in the code.

For a detailed view of the actor's behavior, please see the implementation in the `Account.java` file

### 11.1.3
**General:**
Like the Account, the Bank is also defined by the createReceive method which 
maps the types to the right method. The onTransaction handler takes two accounts anc calls 
`.Withdraw()` and `.Deposit()` on them respectively.

**Messages:**
The only message within the `Bank.java` is the Transaction-message. 
It holds a reference to two unique account and the amount that is to be transferred. 
The message itself is immutable like the previous.

**Initialization**
A bank is initialized a bankId and the ActorContext context which is 
necessary for it to interact with the system. Like the previous, we made static 
create method which serves as a factory, abstracting the instantiation process and returning 
the initial behavior setup of the actor.

**Message Handling**
The onTransaction simply calls Withdraw() and Deposit() on the given actor-references and 
with the given amount.

### 11.1.4
**State Elements:** The MobileApp class maintains a static mobileAppCount to track the number of instances and 
assigns a unique mobileAppId to each instance for identification and logging purposes. 
It also includes a Random instance for generating the random transaction amounts in the `MakePayments` message.

**Initialization:** The actor is initialized using Akka's Behaviors.setup, incrementing mobileAppCount and setting mobileAppId. 
This setup is essential for preparing the actor's unique state and readiness. It is 
called with the `create()` method, which returns the initial behavior setup of the actor.

**Purpose of Messages:** The `Start` serves as a placeholder for future start-up configurations.
`MakePayments` triggers payment processing, supporting both multiple random transactions and single fixed-amount 
transactions, determined by the multipleTransactions flag. This design offers us the opportunity for different payment 
scenarios like the one described in 11.1.5.

For a detailed view of the MobileApp behavior, please see the implementation in the `MobileApp.java` file


### 11.1.5

See the implementation of class `MakeTransaction` and the Behavior 
onMakeTransaction in guardian.java file as well as the message passing 
by guardian.tell function call in main.

```java
		guardian.tell(new Guardian.MakeTransaction(...);
```

### 11.1.6

See the implementation of the class `MakePayments` in `MobileApp.java` file and follow 
the multipleTransactions field.

### 11.1.7
The balance printed in response to the `PrintBalance` message in an Akka actor system 
can vary depending on several factors, primarily due to the asynchronous nature of 
message processing in Akka. In our opinion there are three possibilities:

**Before All Payments Are Made:**
If the PrintBalance message is processed before any Deposit or Withdraw messages, 
it will print the balance as it was initially set or last modified before these payment messages were handled.

**After All Payments Are Made:** If the PrintBalance message is processed after all Deposit 
and Withdraw messages, it will print the balance reflecting all the changes made by these payments.

*In our implementation*
**In Between Payment Transactions:** In our current environment where multiple messages
are sent to the actor at nearly the same time, the PrintBalance message might be 
processed while some payments have been processed with onWithDraw and onDeposite, and others have not.
This scenario would print a balance that reflects only the payments processed up to that point.


Please look at the implementation of the `PrintBalance` message in the `Account.java` file
and the message handle `onPrintBalance` in the same file.


### 11.1.8

Due to the nature of the Actors-model, the deposit-messages will be ordered in a FIFO-style in the mailbox of 
the account-actor. However, we CANNOT make any assumptions about the order of arrival of the messages since it is not 
guaranteed that m1 will arrive or after m2 (or the other way around). Both b1 and b2 will place each of their messages 
in some order within the FIFO-mailbox of the account-actor. Thus, when the account-actor updates its balance, it does 
so by handling each message one after one (sequentially).
This also means that *no data race will occur* since each message is handlede in a FIFO-manner (whatever the order is)  