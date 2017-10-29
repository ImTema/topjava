package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        for(UserMealWithExceed meal :getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000)){
            System.out.println(meal.isExceed());
        }
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExceed> resultList = new ArrayList<>();
        Map<LocalDate, Integer> treeMap = new TreeMap<>();
        for(UserMeal meal : mealList){
            treeMap.compute(meal.getDateTime().toLocalDate(), (date, cal )-> (cal==null? meal.getCalories(): cal+meal.getCalories()));
        }

        for(Map.Entry<LocalDate, Integer> entry : treeMap.entrySet()) {
            LocalDate key = entry.getKey();
            Integer value = entry.getValue();
            if(value>caloriesPerDay){
                for(UserMeal meal : mealList){
                    if(meal.getDateTime().toLocalDate().equals(key)){
                        UserMealWithExceed umwe=new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), true);
                        if(TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)){
                            resultList.add(umwe);
                        }
                    }
                }
            }else{
                for(UserMeal meal : mealList){
                    if(meal.getDateTime().toLocalDate().equals(key)){
                        UserMealWithExceed umwe=new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), false);
                        if(TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)){
                            resultList.add(umwe);
                        }
                    }
                }
            }
        }
        return resultList;
    }
}
