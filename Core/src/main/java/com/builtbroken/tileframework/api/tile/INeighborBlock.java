package com.builtbroken.tileframework.api.tile;

import com.builtbroken.mc.lib.transform.vector.Pos;
import net.minecraft.block.Block;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/11/2016.
 */
public interface INeighborBlock extends ITile
{
    void onNeighborChanged(Block block);

    void onNeighborChanged(Pos pos);
}
