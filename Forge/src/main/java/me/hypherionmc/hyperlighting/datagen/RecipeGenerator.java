package me.hypherionmc.hyperlighting.datagen;

import me.hypherionmc.hyperlighting.common.init.HLBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

/**
 * @author HypherionSA
 * @date 04/08/2022
 */
public class RecipeGenerator extends RecipeProvider {

    public RecipeGenerator(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        System.out.println("Running Recipe Gen");

        ShapedRecipeBuilder.shaped(HLBlocks.ADVANCED_TORCH)
                .pattern(" X ")
                .pattern(" Y ")
                .pattern("   ")
                .define('X', ItemTags.WOOL)
                .define('Y', Items.STICK)
                .unlockedBy("wool", has(ItemTags.WOOL))
                .save(consumer);

    }
}
