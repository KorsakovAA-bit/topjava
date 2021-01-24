package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.*;
import java.time.chrono.ChronoLocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        //List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        //mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles
        List<UserMealWithExcess> userMealWithExcesses = new ArrayList<>();
        Map<LocalDate, Integer> caloriesPerDate = new HashMap<>();

        Integer secondsForStartTime = startTime.getHour()*360 + startTime.getMinute()*60 + startTime.getSecond();
        Integer secondsForEndTime = endTime.getHour()*360 + endTime.getMinute()*60 + endTime.getSecond();

        for (UserMeal userMeal: meals) {
            LocalDateTime timeForCurrentMeal = userMeal.getDateTime();
            if(caloriesPerDate.containsKey(LocalDate.of(timeForCurrentMeal.getYear(), timeForCurrentMeal.getMonth(), timeForCurrentMeal.getDayOfMonth())))
                caloriesPerDate.put(LocalDate.of(timeForCurrentMeal.getYear(), timeForCurrentMeal.getMonth(), timeForCurrentMeal.getDayOfMonth()), caloriesPerDate.get(LocalDate.of(timeForCurrentMeal.getYear(), timeForCurrentMeal.getMonth(), timeForCurrentMeal.getDayOfMonth())) + userMeal.getCalories());
            else caloriesPerDate.put(LocalDate.of(timeForCurrentMeal.getYear(), timeForCurrentMeal.getMonth(), timeForCurrentMeal.getDayOfMonth()), userMeal.getCalories());
        }

        for (UserMeal userMeal: meals) {
            LocalDateTime timeForCurrentMeal = userMeal.getDateTime();
            Integer secondsForCurrentMeal = timeForCurrentMeal.getHour()*360 + timeForCurrentMeal.getMinute()*60 + timeForCurrentMeal.getSecond();

            if(secondsForCurrentMeal >= secondsForStartTime && secondsForCurrentMeal < secondsForEndTime ) {
                if(caloriesPerDate.get(LocalDate.of(timeForCurrentMeal.getYear(), timeForCurrentMeal.getMonth(), timeForCurrentMeal.getDayOfMonth())) > caloriesPerDay)
                userMealWithExcesses.add(new UserMealWithExcess(timeForCurrentMeal, userMeal.getDescription(), userMeal.getCalories(), true));
                else userMealWithExcesses.add(new UserMealWithExcess(timeForCurrentMeal, userMeal.getDescription(), userMeal.getCalories(), false));
            }
        }

        return userMealWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        List<UserMealWithExcess> userMealWithExcesses;
        Map<LocalDate, Integer> caloriesPerDate = new HashMap<>();

        Integer secondsForStartTime = startTime.getHour()*360 + startTime.getMinute()*60 + startTime.getSecond();
        Integer secondsForEndTime = endTime.getHour()*360 + endTime.getMinute()*60 + endTime.getSecond();

        meals.stream().forEach(e -> {
            LocalDateTime timeForCurrentMeal = e.getDateTime();
            if(caloriesPerDate.containsKey(LocalDate.of(timeForCurrentMeal.getYear(), timeForCurrentMeal.getMonth(), timeForCurrentMeal.getDayOfMonth())))
                caloriesPerDate.put(LocalDate.of(timeForCurrentMeal.getYear(), timeForCurrentMeal.getMonth(), timeForCurrentMeal.getDayOfMonth()), caloriesPerDate.get(LocalDate.of(timeForCurrentMeal.getYear(), timeForCurrentMeal.getMonth(), timeForCurrentMeal.getDayOfMonth())) + e.getCalories());
            else caloriesPerDate.put(LocalDate.of(timeForCurrentMeal.getYear(), timeForCurrentMeal.getMonth(), timeForCurrentMeal.getDayOfMonth()), e.getCalories());
        });

        userMealWithExcesses = meals.stream().filter(e -> {
            LocalDateTime timeForCurrentMeal = e.getDateTime();
            Integer secondsForCurrentMeal = timeForCurrentMeal.getHour()*360 + timeForCurrentMeal.getMinute()*60 + timeForCurrentMeal.getSecond();
            return secondsForCurrentMeal >= secondsForStartTime && secondsForCurrentMeal < secondsForEndTime;
        }).map(e -> {
            if(caloriesPerDate.get(LocalDate.of(e.getDateTime().getYear(), e.getDateTime().getMonth(), e.getDateTime().getDayOfMonth())) > caloriesPerDay)
             return new UserMealWithExcess(e.getDateTime(), e.getDescription(), e.getCalories(), true);
            else return new UserMealWithExcess(e.getDateTime(), e.getDescription(), e.getCalories(), false);
        }).collect(Collectors.toList());


        return userMealWithExcesses;
    }
}
