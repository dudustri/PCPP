package mobilepayment;

import akka.actor.typed.ActorSystem;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {

		// start actor system
		final ActorSystem<Guardian.GuardianCommand> guardian = ActorSystem.create(Guardian.create(),
				"mobile_pay_system");

		// send initial message
		guardian.tell(new Guardian.KickOff());

		double defaultAmount = 35.20;
		double bigAmount = 800;

		guardian.tell(new Guardian.MakeTransaction("mb1", "b1", "a1", "a2", false, defaultAmount));
		guardian.tell(new Guardian.MakeTransaction("mb2", "b2", "a2", "a1", false, bigAmount));

		// guardian.tell(new Guardian.MakeTransaction("mb1", "b1", "a1", "a2", true,
		// defaultAmount));
		// guardian.tell(new Guardian.MakeTransaction("mb2", "b2", "a1", "a2", true,
		// defaultAmount));

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		guardian.tell(new Guardian.GetBalance("a1"));
		guardian.tell(new Guardian.GetBalance("a2"));

		// wait until user presses enter
		try {
			System.out.println(">>> Press ENTER to exit <<<");
			System.in.read();
		} catch (IOException e) {
			System.out.println("Error " + e.getMessage());
			e.printStackTrace();
		} finally {
			// terminate actor system execution
			// To be implemented
		}

	}

}
