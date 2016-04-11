package com.builtbroken.tileframework.api;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/11/2016.
 */
public interface IMod
{
    /** Prefix used by the mod before resources, example( Mod: ) */
    String getPrefix();

    /** Resource folder name, used to pull localization and textures */
    String getDomain();
}
