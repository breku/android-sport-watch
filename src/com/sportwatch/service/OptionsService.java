package com.sportwatch.service;

/**
 * User: Breku
 * Date: 10.02.14
 */
public class OptionsService extends BaseService {

    private DatabaseHelper databaseHelper = new DatabaseHelper(activity);

    public Integer getNumberOfHandClocks(){
        return databaseHelper.getNumberOfHandClocks();
    }


    public void updateNumberOfHandClocks(int numberOfHandClocks) {
        databaseHelper.updateNumberOfHandClocks(numberOfHandClocks);
    }
}
