package org.example;



public enum District {
    MAADI,
    DOKKI,
    FAISAL,
    NASR_CITY,
    HELIOPOLIS;



    public static  void optionDistrict() {;
        System.out.println("0. "+MAADI);
        System.out.println("1. "+DOKKI);
        System.out.println("2. "+FAISAL);
        System.out.println("3. "+NASR_CITY);
        System.out.println("4. "+HELIOPOLIS);
    }
    public static District district(int choice){
        return switch (choice) {
            case 0 -> MAADI;
            case 1 -> DOKKI;
            case 2 -> FAISAL;
            case 3 -> NASR_CITY;
            case 4 -> HELIOPOLIS;
            default -> null;
        };

    }
}


