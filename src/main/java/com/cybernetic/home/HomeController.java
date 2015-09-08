package com.cybernetic.home;

import java.security.Principal;

import com.cybernetic.example.SimpleExample;
import com.cybernetic.mapquest.MapQuestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.cybernetic.mapquest.constants.RouteResponseParameters.*;

@Controller
public class HomeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Principal principal) {
		SimpleExample.test();
		LOGGER.info(new MapQuestClient().getRoute().findValue(DISTANCE).toString());
		return principal != null ? "home/homeSignedIn" : "home/homeNotSignedIn";
	}
}
