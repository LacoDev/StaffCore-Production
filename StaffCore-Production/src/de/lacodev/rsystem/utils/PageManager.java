package de.lacodev.rsystem.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import de.lacodev.rsystem.objects.BanReasons;
import de.lacodev.rsystem.objects.MuteReasons;
import de.lacodev.rsystem.objects.Reasons;

public class PageManager {
	
    public static HashMap<Player, Integer> page = new HashMap<>();

    public static List<Reasons> getPageItems(List<Reasons> items, int page, int spaces) {


        int upperBound = page * spaces;

        int lowerBound = upperBound - spaces;


        List<Reasons> reasons = new ArrayList<>();

        for (int i = lowerBound; i < upperBound; i++) {

                try {

                    reasons.add(new Reasons(items.get(i).getItem(), items.get(i).getName()));

                } catch(IndexOutOfBoundsException e) {

                    continue;

                }

        }

        return reasons;

    }



    public static boolean isPageValid(List<Reasons> items, int page, int spaces) {

        if (page <= 0) {

            return false;

        }


        int upperBound = page * spaces;

        int lowerBound = upperBound - spaces;



        return items.size() > lowerBound;

    }
    
    public static List<BanReasons> getPageItems2(List<BanReasons> items, int page, int spaces) {

        int upperBound = page * spaces;
        int lowerBound = upperBound - spaces;

        List<BanReasons> reasons = new ArrayList<>();
        for (int i = lowerBound; i < upperBound; i++) {
            try {
                reasons.add(new BanReasons(items.get(i).getName(), items.get(i).getID()));
            } catch(IndexOutOfBoundsException e) {
                continue;
            }
        }
        return reasons;
    }

    public static boolean isPageValid2(List<BanReasons> items, int page, int spaces) {
        if (page <= 0) {
            return false;
        }

        int upperBound = page * spaces;
        int lowerBound = upperBound - spaces;

        return items.size() > lowerBound;
    }

    public static List<MuteReasons> getPageItems3(List<MuteReasons> items, int page, int spaces) {

        int upperBound = page * spaces;
        int lowerBound = upperBound - spaces;

        List<MuteReasons> reasons = new ArrayList<>();
        for (int i = lowerBound; i < upperBound; i++) {
            try {
                reasons.add(new MuteReasons(items.get(i).getName(), items.get(i).getID()));
            } catch(IndexOutOfBoundsException e) {
                continue;
            }
        }
        return reasons;
    }

    public static boolean isPageValid3(List<MuteReasons> items, int page, int spaces) {
        if (page <= 0) {
            return false;
        }

        int upperBound = page * spaces;
        int lowerBound = upperBound - spaces;

        return items.size() > lowerBound;
    }	

}
