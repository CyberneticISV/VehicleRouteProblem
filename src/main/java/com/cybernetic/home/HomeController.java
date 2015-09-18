package com.cybernetic.home;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;

import com.cybernetic.example.CostMatrixVRPTW;
import com.cybernetic.example.SimpleExample;
import com.cybernetic.mapquest.MapQuestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.cybernetic.mapquest.constants.RouteResponseParameters.*;

@Controller
public class HomeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Principal principal) {
		return "home/demo";
	}
}
