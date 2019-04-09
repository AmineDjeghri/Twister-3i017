package test.servicesTests;

import org.json.JSONObject;

import services.TwistS;

public class MessageWSTests {

	public static void main(String[] args) {
		JSONObject ob = TwistS.addTwist("123", "message Test");
		System.out.println(ob);

	}

}
