package dev.nottekk.notvolt.utils.apis;

import de.presti.amari4j.base.Amari4J;
import dev.nottekk.notvolt.main.Main;
import lombok.Getter;

/**
 * AmariAPI.
 */
public class AmariAPI {

    /**
     * Constructor used to load the API-Wrapper.
     */
    public AmariAPI() {
        amari4J = new Amari4J(Main.getInstance().getConfig().getConfiguration().getString("amari.apitoken"));
    }

    /**
     * Instance of the Amari4J Wrapper.
     */
    @Getter
    private static Amari4J amari4J;
}
