package com.kristurek.polskatv.iptv;

public enum ServiceProvider {

    POLBOX(0, "Polbox", "com.kristurek.polskatv.iptv.polbox.PolboxService"),
    POLSKA_TELEWIZJA_USA(1, "Polska Telewizja USA", "com.kristurek.polskatv.iptv.polskatelewizjausa.PolskaTelewizjaUsaService");

    private Integer id;
    private String clazz;
    private String name;

    ServiceProvider(Integer id, String name, String clazz) {
        this.id = id;
        this.name = name;
        this.clazz = clazz;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getClazz() {
        return clazz;
    }

    public static ServiceProvider valueOfClazz(Integer mId) {
        if (mId == null)
            throw new IllegalArgumentException();
        for (ServiceProvider sp : values())
            if (sp.id.equals(mId))
                return sp;

        throw new IllegalArgumentException();
    }

    @Override
    public String toString() {
        return "ServiceProvider{" +
                "id=" + id +
                ", clazz='" + clazz + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
