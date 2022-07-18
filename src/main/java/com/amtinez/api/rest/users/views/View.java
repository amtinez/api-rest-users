package com.amtinez.api.rest.users.views;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
public final class View {

    private View() {
    }

    public interface Anonymous {

    }

    public interface User extends Anonymous {

    }

    public interface Admin extends User {

    }

}
