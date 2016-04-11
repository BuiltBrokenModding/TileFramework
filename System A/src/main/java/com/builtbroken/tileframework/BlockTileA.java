package com.builtbroken.tileframework;

import com.builtbroken.mc.lib.transform.region.Cube;
import com.builtbroken.mc.lib.transform.vector.Point;
import com.builtbroken.mc.lib.transform.vector.Pos;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Wrapper class for senting Block method calls to {@link TileData} object and  {@link TileA} object.
 *
 * @author Dark
 */
public class BlockTileA extends BlockContainer
{
    public final TileData tileData;

    public BlockTileA(TileData data)
    {
        super(data.material);
        this.tileData = data;

        //Kill the game if the data is wrong
        if (tileData == null)
        {
            throw new IllegalArgumentException("Tile data can not be null");
        }
        //Init missing data from tileData object
        this.tileData.block = this;

        //Load all data from tile data object
        if (tileData != null)
        {
            this.setBlockBounds((float) this.tileData.bounds.min().x(), (float) this.tileData.bounds.min().y(), (float) this.tileData.bounds.min().z(), (float) this.tileData.bounds.max().x(), (float) this.tileData.bounds.max().y(), (float) this.tileData.bounds.max().z());
        }

        this.opaque = isOpaqueCube();
        setBlockName(tileData.mod.getPrefix() + tileData.name);
        setBlockTextureName(tileData.mod.getPrefix() + tileData.textureName);
        setCreativeTab(tileData.creativeTab == null ? CreativeTabs.tabMisc : tileData.creativeTab);
        setLightOpacity(isOpaqueCube() ? 255 : 0);
        setHardness(tileData.hardness);
        setResistance(tileData.resistance);
        setStepSound(tileData.stepSound);
    }

    @Override
    public TileEntity createTileEntity(World world, int meta)
    {
        return tileData.createNewTileEntity(world, meta);
    }


    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return tileData.createNewTileEntity(world, meta);
    }

    @Override
    public void fillWithRain(World world, int x, int y, int z)
    {
        inject(world, x, y, z);
        getTile(world, x, y, z).onFillRain();
        eject();
    }

    @Override
    public float getExplosionResistance(Entity entity)
    {
        return tileData.staticTile.getExplosionResistance(entity);
    }

    @Override
    public float getExplosionResistance(Entity entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ)
    {
        inject(world, x, y, z);
        float resistance = getTile(world, x, y, z).getExplosionResistance(entity, new Pos(explosionX, explosionY, explosionZ));
        eject();
        return resistance;
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player)
    {
        inject(world, x, y, z);
        getTile(world, x, y, z).onPlayerLeftClick(player);
        eject();
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        inject(world, x, y, z);
        getTile(world, x, y, z).onAdded();
        eject();
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        inject(world, x, y, z);
        getTile(world, x, y, z).onPlaced(entityLiving, itemStack);
        eject();
    }

    @Override
    public void onPostBlockPlaced(World world, int x, int y, int z, int metadata)
    {
        inject(world, x, y, z);
        getTile(world, x, y, z).onPostPlaced(metadata);
        eject();
    }

    /**
     * Called upon the block being destroyed by an explosion
     */
    @Override
    public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion ex)
    {
        inject(world, x, y, z);
        getTile(world, x, y, z).onDestroyedByExplosion(ex);
        eject();
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int par6)
    {
        inject(world, x, y, z);
        getTile(world, x, y, z).onRemove(block, par6);
        eject();
        super.breakBlock(world, x, y, z, block, par6);
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
    {
        inject(world, x, y, z);
        boolean b = getTile(world, x, y, z).removeByPlayer(player, willHarvest);
        eject();
        return b;
    }

    @Override
    public int quantityDropped(int meta, int fortune, Random random)
    {
        return tileData.staticTile.quantityDropped(meta, fortune);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        inject(world, x, y, z);
        getTile(world, x, y, z).onNeighborChanged(block);
        eject();
    }

    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side)
    {
        inject(world, x, y, z);
        boolean b = getTile(world, x, y, z).canPlaceBlockOnSide(ForgeDirection.getOrientation(side));
        eject();
        return b;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        inject(world, x, y, z);
        boolean b = getTile(world, x, y, z).canPlaceBlockAt();
        eject();
        return b;
    }

    @Override
    public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ)
    {
        inject(world, x, y, z);
        getTile(world, x, y, z).onNeighborChanged(new Pos(tileX, tileY, tileZ));
        eject();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        inject(world, x, y, z);
        boolean value = getTile(world, x, y, z).onPlayerActivated(player, side, new Pos(hitX, hitY, hitZ));
        eject();
        return value;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random par5Random)
    {
        inject(world, x, y, z);
        getTile(world, x, y, z).blockUpdate();
        eject();
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random par5Random)
    {
        inject(world, x, y, z);
        getTile(world, x, y, z).randomDisplayTick();
        eject();
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        inject(world, x, y, z);
        getTile(world, x, y, z).onCollide(entity);
        eject();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity)
    {
        inject(world, x, y, z);
        Iterable<Cube> bounds = getTile(world, x, y, z).getCollisionBoxes(new Cube(aabb).subtract(new Pos(x, y, z)), entity);
        eject();
        if (bounds != null)
        {
            for (Cube cuboid : bounds)
            {
                AxisAlignedBB bb = cuboid.toAABB().offset(x, y, z);
                if (aabb.intersectsWith(bb))
                {
                    list.add(bb);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
        inject(world, x, y, z);
        TileA tile = getTile(world, x, y, z);
        AxisAlignedBB value = tile.getSelectBounds().clone().add(tile.x(), tile.y(), tile.z()).toAABB();
        eject();
        return value;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        inject(world, x, y, z);
        TileA tile = getTile(world, x, y, z);
        AxisAlignedBB value = tile.getCollisionBounds().clone().add(tile.x(), tile.y(), tile.z()).toAABB();
        eject();
        return value;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side)
    {
        inject(access, x, y, z);
        boolean value = tileData.staticTile.shouldSideBeRendered(side);
        eject();
        return value;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess access, int x, int y, int z, int side)
    {
        inject(access, x, y, z);
        boolean value = getTile(access, x, y, z).isSolid(side);
        eject();
        return value;
    }

    @Override
    public int getLightValue(IBlockAccess access, int x, int y, int z)
    {
        int value = 0;
        if (access != null)
        {
            inject(access, x, y, z);
            value = getTile(access, x, y, z).getLightValue();
            eject();
        }
        return value;
    }

    @Override
    public boolean hasComparatorInputOverride()
    {
        //TODO, add support for comparators
        return false;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return tileData.staticTile == null || tileData.isOpaque;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return tileData.renderNormalBlock;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderType()
    {
        return tileData.renderNormalBlock ? 0 : tileData.renderType;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(IBlockAccess access, int x, int y, int z, int side)
    {
        inject(access, x, y, z);
        IIcon value = getTile(access, x, y, z).getIcon(side, access.getBlockMetadata(x, y, z));
        eject();
        return value;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta)
    {
        return tileData.staticTile.getIcon(side, meta);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        tileData.staticTile.registerIcons(iconRegister);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int colorMultiplier(IBlockAccess access, int x, int y, int z)
    {
        inject(access, x, y, z);
        int value = getTile(access, x, y, z).getColorMultiplier();
        eject();
        return value;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getBlockColor()
    {
        return tileData.staticTile.getBlockColor();
    }

    /**
     * Returns the color this block should be rendered. Used by leaves.
     */
    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderColor(int i)
    {
        return tileData.staticTile.getRenderColor(i);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player)
    {
        inject(world, x, y, z);
        ItemStack value = getTile(world, x, y, z).getPickBlock(target);
        eject();
        return value;
    }

    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        inject(world, x, y, z);
        ArrayList<ItemStack> value = getTile(world, x, y, z).getDrops(metadata, fortune);
        eject();
        return value != null ? value : new ArrayList<ItemStack>();
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list)
    {
        tileData.staticTile.getSubBlocks(item, creativeTabs, list);
    }

    /**
     * Redstone interaction
     */
    @Override
    public boolean canProvidePower()
    {
        return tileData.canEmmitRedstone;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess access, int x, int y, int z, int side)
    {
        inject(access, x, y, z);
        int value = getTile(access, x, y, z).getWeakRedstonePower(side);
        eject();
        return value;
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess access, int x, int y, int z, int side)
    {
        inject(access, x, y, z);
        int value = getTile(access, x, y, z).getStrongRedstonePower(side);
        eject();
        return value;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, int x, int y, int z)
    {
        inject(access, x, y, z);
        getTile(access, x, y, z).setBlockBoundsBasedOnState();
        eject();
    }

    @Override
    protected void dropBlockAsItem(World world, int x, int y, int z, ItemStack itemStack)
    {
        if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops"))
        {
            InventoryUtility.dropItemStack(world, new Pos(x, y, z), itemStack);
        }
    }

    @Override
    public int getRenderBlockPass()
    {
        return tileData.staticTile.getRenderBlockPass();
    }

    @Override
    public int tickRate(World world)
    {
        inject(world, 0, 0, 0);
        int t = tileData.staticTile.tickRate();
        eject();
        return t;

    }

    public static Point getClickedFace(Byte hitSide, float hitX, float hitY, float hitZ)
    {
        switch (hitSide)
        {
            case 0:
                return new Point(1 - hitX, hitZ);
            case 1:
                return new Point(hitX, hitZ);
            case 2:
                return new Point(1 - hitX, 1 - hitY);
            case 3:
                return new Point(hitX, 1 - hitY);
            case 4:
                return new Point(hitZ, 1 - hitY);
            case 5:
                return new Point(1 - hitZ, 1 - hitY);
            default:
                return new Point(0.5, 0.5);
        }
    }


    /**
     * Injects and eject();s data from the TileEntity.
     */
    public void inject(IBlockAccess access, int x, int y, int z)
    {
        if (access instanceof World)
        {
            tileData.staticTile.setWorldObj(((World) access));
        }

        tileData.staticTile.setAccess(access);
        tileData.staticTile.xCoord = x;
        tileData.staticTile.yCoord = y;
        tileData.staticTile.zCoord = z;
    }

    public void eject()
    {
        tileData.staticTile.setWorldObj(null);
        tileData.staticTile.xCoord = 0;
        tileData.staticTile.yCoord = 0;
        tileData.staticTile.zCoord = 0;
    }

    public TileA getTile(IBlockAccess world, int x, int y, int z)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileA)
        {
            return ((TileA) tile);
        }
        return tileData.staticTile;
    }
}
