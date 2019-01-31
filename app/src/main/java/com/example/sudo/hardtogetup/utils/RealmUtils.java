package com.example.sudo.hardtogetup.utils;

import com.example.sudo.hardtogetup.models.MathProblem;

import java.util.List;

import io.realm.Realm;

public class RealmUtils {
    //realm metoda koja dohvaća sve probleme
    public static List<MathProblem> getAllMAthProblems() {
        Realm realm = Realm.getDefaultInstance();
        List<MathProblem> resultList = realm.copyFromRealm(realm.where(MathProblem.class).findAll());
        realm.close();
        return resultList;
    }
}
