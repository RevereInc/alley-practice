package dev.revere.alley.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.adapter.core.listener.CoreChatListener;
import dev.revere.alley.api.menu.MenuListener;
import dev.revere.alley.base.arena.listener.ArenaListener;
import dev.revere.alley.base.combat.listener.CombatListener;
import dev.revere.alley.base.hotbar.listener.HotbarListener;
import dev.revere.alley.base.queue.listener.QueueListener;
import dev.revere.alley.base.server.listener.CraftingListener;
import dev.revere.alley.base.spawn.listener.SpawnListener;
import dev.revere.alley.feature.emoji.listener.EmojiListener;
import dev.revere.alley.feature.item.listener.ItemListener;
import dev.revere.alley.feature.layout.listener.LayoutListener;
import dev.revere.alley.game.ffa.listener.FFAListener;
import dev.revere.alley.game.ffa.listener.FFABlockListener;
import dev.revere.alley.game.ffa.listener.FFACuboidListener;
import dev.revere.alley.game.ffa.listener.FFADamageListener;
import dev.revere.alley.game.ffa.listener.FFADisconnectListener;
import dev.revere.alley.game.match.listener.MatchListener;
import dev.revere.alley.game.match.listener.impl.*;
import dev.revere.alley.game.match.snapshot.listener.SnapshotListener;
import dev.revere.alley.game.party.listener.PartyListener;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.profile.listener.ProfileListener;

import java.util.Arrays;

/**
 * @author Emmy
 * @project alley-practice
 * @since 16/07/2025
 */
@Service(provides = ListenerService.class, priority = 1000)
public class ListenerServiceImpl implements ListenerService {

    @Override
    public void initialize(AlleyContext context) {
        this.registerListeners(context.getPlugin());
    }

    @Override
    public void registerListeners(Alley plugin) {
        Arrays.asList(
                new ProfileListener(),
                new HotbarListener(),
                new PartyListener(),
                new ArenaListener(),
                new MenuListener(),
                new SpawnListener(),

                new EmojiListener(),
                new CombatListener(),
                new QueueListener(),
                new CoreChatListener(),
                new LayoutListener(),
                new SnapshotListener(),
                new CraftingListener(),

                new ItemListener(),

                new FFAListener(), new FFACuboidListener(),
                new FFABlockListener(), new FFADamageListener(), new FFADisconnectListener(),

                new MatchListener(), new MatchInteractListener(),
                new MatchPearlListener(), new MatchDisconnectListener(),
                new MatchDamageListener(), new MatchChatListener(), new MatchBlockListener()

        ).forEach(listener -> plugin.getServer().getPluginManager().registerEvents(listener, plugin));
    }
}