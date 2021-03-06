package com.tin2.secondapp.controller;

import com.hazelcast.core.HazelcastInstance;
import com.tin2.secondapp.HazelcastHttpSessionConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.session.MapSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by VAN_TIEN on 08/04/2017.
 */
@Controller
public class HomeController {
    public static final String MESSAGES = "messages";

    @Autowired
    HazelcastInstance hazelcastInstance;

    @RequestMapping( value = "/", method = RequestMethod.GET )
    public ModelAndView index(HttpSession session) {
        Map<String, Object> map = hazelcastInstance.getMap(HazelcastHttpSessionConfiguration.NAMESPACE);
        for (String key : map.keySet()) {
            MapSession mapSession = (MapSession) map.get(key);
            for (String key2 : mapSession.getAttributeNames()) {
                if ("SPRING_SECURITY_CONTEXT".equals(key2)) {
                    SecurityContext securityContext = mapSession.getAttribute(key2);
                    Authentication authentication = securityContext.getAuthentication();
                    session.setAttribute("user", ((User) authentication.getPrincipal()).getUsername());
                }
            }
        }
        for (String key : map.keySet()) {
            MapSession mapSession = (MapSession) map.get(key);
            session.setAttribute(MESSAGES, mapSession.getAttribute(MESSAGES));
        }
        if( session.getAttribute(MESSAGES) == null ) {
            session.setAttribute(MESSAGES, new ArrayList<>() );
        }
        ModelAndView mv = new ModelAndView();
        mv.setViewName( "welcome" );
        return mv;
    }
}
