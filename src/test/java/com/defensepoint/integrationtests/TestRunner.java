package com.defensepoint.integrationtests;

import org.junit.runner.JUnitCore;

public class TestRunner {
    public static void main(String[] args) {
        if(args.length == 1 && args[0].equalsIgnoreCase("21points")) {
            JUnitCore.runClasses(Points21BloodPressureResourceIT.class);
        } if(args.length == 1 && args[0].equalsIgnoreCase("online-store")) {
            JUnitCore.runClasses(OnlineStoreItemControllerIT.class);
        } if(args.length == 1 && args[0].equalsIgnoreCase("keycloak")) {
            JUnitCore.runClasses(KeyCloakUserIT.class);
        } if(args.length == 1 && args[0].equalsIgnoreCase("bouncycastle")) {
            JUnitCore.runClasses(BouncyCastleIT.class);
        } else {
            System.out.println("WRONG ARGUMENT");
            System.out.println("Possible arguments: 21points, online-store, keycloak, bouncycastle");
        }
    }
}