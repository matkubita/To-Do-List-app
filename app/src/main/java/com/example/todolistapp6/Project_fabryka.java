package com.example.todolistapp6;

import android.content.res.Resources;
import android.graphics.Color;

import java.util.ArrayList;

public class Project_fabryka {
    ArrayList<Project_single> lista_projektow;

    public Project_fabryka() {
        this.lista_projektow = new ArrayList<>();
    }

    public void usun_projekt(String nazwa){

        for (int i  =0 ; i< lista_projektow.size(); i ++){
            if (lista_projektow.get(i).getName().equals(nazwa)){
                lista_projektow.remove(i);
            }
        }


    }

    public void print_lista_projektow(){
        System.out.println(lista_projektow);
    }

    public ArrayList<Project_single> getLista_projektow() {
        return lista_projektow;
    }

    public void dodaj_projekt(String name, int color){
        Project_single projekt_new = new Project_single(name,color);
        this.lista_projektow.add(projekt_new);
    }

    public class Project_single{
        String name;
        int color;

        public Project_single(String name, int color) {
            this.name = name;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Project_single{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

}
