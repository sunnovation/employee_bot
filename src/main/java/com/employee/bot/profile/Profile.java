package com.employee.bot.profile;



public record Profile(
        String id,
        String name,
        String designation,
        String department,
        int age,
        Double salary,
        String bio,
        Gender gender,
        String imageUrl
) {

}
