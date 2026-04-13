package com.last_battle.kalah_server.controller;

import com.last_battle.kalah_server.model.User;
import com.last_battle.kalah_server.service.UsersService;
import io.javalin.http.Context;

import java.util.List;

public class UsersController {
    private final UsersService usersService;
    
    public UsersController() {
        this.usersService = new UsersService();
    }
    
    public void getTopUsers(Context ctx) {
        System.out.println("onTop" + ctx.body());
        String limitParam = ctx.queryParam("limit");
        int limit = limitParam != null ? Integer.parseInt(limitParam) : 10;
        
        List<User> topUsers = usersService.getTopUsers(limit);
        ctx.json(topUsers);
    }
}