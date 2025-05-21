/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Coffee_JavaClasses;

import java.util.Random;

/**
 *
 * @author ADMIN
 */
public class Verification_Code {
    public static String generateCode() {
        Random rand = new Random();
        int code = 100000 + rand.nextInt(900000);
        return String.valueOf(code);
        }
    }

