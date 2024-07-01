// REMINDER: You should only modify the Server.java file
package mathsserver;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

import java.util.stream.IntStream;

public class Guardian extends AbstractBehavior<Guardian.KickOff> {

    /* --- Messages ------------------------------------- */
    public static final class KickOff { }

    /* --- State ---------------------------------------- */
    // empty

    /* --- Constructor ---------------------------------- */
    private Guardian(ActorContext<KickOff> context) {
		super(context);
    }


    /* --- Actor initial state -------------------------- */
    public static Behavior<KickOff> create() {
		return Behaviors.setup(Guardian::new);
    }
    

    /* --- Message handling ----------------------------- */
    @Override
    public Receive<KickOff> createReceive() {
		return newReceiveBuilder()
			.onMessage(KickOff.class, this::onKickOff)
			.build();
    }


    /* --- Handlers ------------------------------------- */
    public Behavior<KickOff> onKickOff(KickOff msg) {
		final ActorRef<Server.ServerCommand> server =
			getContext().spawn(Server.create(2,8), "server");
		final int N_CLIENTS = 2;
		IntStream
			.range(1,N_CLIENTS+1)
			.forEach(id -> {
					final ActorRef<Client.ClientCommand> client =
						getContext().spawn(Client.create(server), "client_"+id);
					client.tell(new Client.ClientStart());
				});
		return this;
    }
}
