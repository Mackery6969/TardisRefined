package whocraft.tardis_refined.client.model.blockentity.console;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import whocraft.tardis_refined.api.event.TardisClientEvents;
import whocraft.tardis_refined.client.ModelRegistry;
import whocraft.tardis_refined.common.tardis.themes.ConsoleTheme;
import whocraft.tardis_refined.registry.RegistrySupplier;

import java.util.HashMap;
import java.util.Map;

/***
 * A collection of models for rendering the console unit.
 */
public class ConsoleModelCollection {

    public static HashMap<ResourceLocation, ConsoleModelEntry> CONSOLE_MODELS = new HashMap<>();
    private static ConsoleModelCollection instance = null;
    ConsoleUnit toyotaConsoleModel, coralConsoleModel, copperConsoleModel, nukaConsoleModel, factoryConsoleModel, crystalConsoleModel, victorianConsoleModel, mystConsoleModel, initiativeConsoleModel, refurbishedConsoleModel;

    public ConsoleModelCollection() {
        var context = Minecraft.getInstance().getEntityModels();
        this.registerModels(context);
    }

    public static ConsoleModelCollection getInstance() {
        if (ConsoleModelCollection.instance == null) {
            ConsoleModelCollection.instance = new ConsoleModelCollection();
        }

        return instance;
    }

    public void registerModels(EntityModelSet context) {

        factoryConsoleModel = new FactoryConsoleModel(context.bakeLayer(ModelRegistry.FACTORY_CONSOLE));
        nukaConsoleModel = new NukaConsoleModel(context.bakeLayer(ModelRegistry.NUKA_CONSOLE));
        copperConsoleModel = new CopperConsoleModel(context.bakeLayer(ModelRegistry.COPPER_CONSOLE));
        coralConsoleModel = new CoralConsoleModel(context.bakeLayer(ModelRegistry.CORAL_CONSOLE));
        toyotaConsoleModel = new ToyotaConsoleModel(context.bakeLayer(ModelRegistry.TOYOTA_CONSOLE));
        crystalConsoleModel = new CrystalConsoleModel(context.bakeLayer(ModelRegistry.CRYSTAL_CONSOLE));
        victorianConsoleModel = new VictorianConsoleModel(context.bakeLayer(ModelRegistry.VICTORIAN_CONSOLE));
        mystConsoleModel = new MystConsoleModel(context.bakeLayer(ModelRegistry.MYST_CONSOLE));
        initiativeConsoleModel = new InitiativeConsoleModel(context.bakeLayer(ModelRegistry.INITIATIVE_CONSOLE));
        refurbishedConsoleModel = new RefurbishedConsoleModel(context.bakeLayer(ModelRegistry.REFURBISHED_CONSOLE));

        registerModel(ConsoleTheme.FACTORY, factoryConsoleModel);
        registerModel(ConsoleTheme.NUKA, nukaConsoleModel);
        registerModel(ConsoleTheme.COPPER, copperConsoleModel);
        registerModel(ConsoleTheme.CORAL, coralConsoleModel);
        registerModel(ConsoleTheme.TOYOTA, toyotaConsoleModel);
        registerModel(ConsoleTheme.CRYSTAL, crystalConsoleModel);
        registerModel(ConsoleTheme.VICTORIAN, victorianConsoleModel);
        registerModel(ConsoleTheme.MYST, mystConsoleModel);
        registerModel(ConsoleTheme.INITIATIVE, initiativeConsoleModel);
        registerModel(ConsoleTheme.REFURBISHED, refurbishedConsoleModel);

        TardisClientEvents.CONSOLE_MODELS_SETUP.invoker().setupConsoleModels(this, context);

        validateConsoleModels();
    }

    private void registerModel(RegistrySupplier<ConsoleTheme> theme, ConsoleUnit model) {
        registerModel(theme.getId(), new ConsoleModelEntry(model));
    }

    private static void validateConsoleModels() {
        for (Map.Entry<ResourceKey<ConsoleTheme>, ConsoleTheme> entry : ConsoleTheme.CONSOLE_THEME_DEFERRED_REGISTRY.entrySet()) {
            ResourceKey<ConsoleTheme> key = entry.getKey();
            Validate.isTrue(
                    CONSOLE_MODELS.containsKey(key.location()),
                    String.format("Missing registered model for console theme: %s", key)
            );
        }
    }


    public static Logger LOGGER = LogManager.getLogger("TardisRefined/ConsoleModelCollection");

    /**
     * Register a new console model.
     *
     * @param consoleModel The ConsoleUnit to register.
     */
    public void registerModel(ResourceLocation resourceLocation, ConsoleModelEntry consoleModel) {
        if (consoleModel == null) {
            LOGGER.warn("Attempted to register a null console model.");
            return;
        }

        if (CONSOLE_MODELS.containsKey(resourceLocation)) {
            LOGGER.warn("Attempted to register a console model that is already registered: {}", resourceLocation);
            return;
        }

        CONSOLE_MODELS.put(resourceLocation, consoleModel);
        LOGGER.info("Registered console model: {}", resourceLocation);
    }


    public ConsoleModelEntry getConsoleEntry(ResourceLocation themeId) {
        ConsoleModelEntry consoleModelEntry = CONSOLE_MODELS.get(themeId);
        if (consoleModelEntry == null) {
            LOGGER.warn("Could not find model for {}, did you bind it?", themeId);
        }
        return consoleModelEntry;
    }
}
