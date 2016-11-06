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
                // LocalDateTime.of(int year, Month month, int dayOfMonth, int hour, int minute)
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500), // exceed = false
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),   // exceed = false
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),    // exceed = false
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),// exceed = true
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),    // exceed = true
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)     // exceed = true
        );
        //getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
//        .toLocalDate();
//        .toLocalTime();

        for (UserMealWithExceed uList : getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(22, 0), 2000))
        {
            System.out.println(uList);
        }
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with correctly exceeded field
        Map<LocalDate, List<UserMeal>> mapUserMeal = new HashMap<>();
        Map<LocalDate, Boolean> mapExceed = new HashMap<>();
        List<UserMealWithExceed> listUserMealWithExceed = new ArrayList<>();

        // АЛГОРИТМ
        // 1. разделяем еду по датам
        // 2. считаем кол-во калорий в каждой дате, заполняем mapExceed
        // 3. возвращаем список


        // 1. разделяем еду по датам
        for (UserMeal userMeal : mealList) {
            LocalDate localDate = userMeal.getDateTime().toLocalDate();
            if (!mapUserMeal.containsKey(localDate))
                mapUserMeal.put(localDate, new ArrayList<>());

            List<UserMeal> list = mapUserMeal.get(localDate);
            list.add(userMeal);
            mapUserMeal.put(localDate, list);
        }

        // 2. считаем кол-во калорий по каждой дате (один раз для каждой даты)
        for (Map.Entry<LocalDate, List<UserMeal>> map : mapUserMeal.entrySet()) {
            LocalDate dt = map.getKey();
            int sum = 0;
            List<UserMeal> userMealList = mapUserMeal.get(dt);
            for (UserMeal userMeal : userMealList)
                sum += userMeal.getCalories();

            if (sum <= caloriesPerDay) mapExceed.put(dt, false); else mapExceed.put(dt, true);

            // 3. заполняем возвращаемый список
            for (UserMeal userMeal : userMealList)
                if (TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                    listUserMealWithExceed.add(new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), mapExceed.get(dt)));
        }

        return listUserMealWithExceed;
    }
}
