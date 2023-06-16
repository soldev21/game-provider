package com.megafair;

import com.megafair.cache.AuthCachePrefix;
import com.megafair.cache.GameCacheRepository;
import com.megafair.cache.RedisKeyHasher;
import com.megafair.cache.StringCacheRepository;
import com.megafair.cache.model.GameCache;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;


@QuarkusMain
public class GameProvider {
    public static void main(String... args) {
        Quarkus.run(MyApp.class, args);
    }

    @RequiredArgsConstructor
    public static class MyApp implements QuarkusApplication {

        final StringCacheRepository stringCacheRepository;
        final GameCacheRepository gameCacheRepository;

        @Override
        public int run(String... args) throws Exception {
            System.out.println("Starting MODULE GP");
            // PLATFORM 1
            String pId1 = "4ae6f048-b8a3-4776-bffa-193f7b245a9d";
            String sKey1 = "004e258c-adc7-4835-b86a-8b229568e997";
            String pd1cs1 = "40b74e2c-7ff3-4c41-94c0-fe9e945538a5";
            String pd1cs2 = "64df7b12-d0f0-4df7-9c20-6b296d039708";

            // PLATFORM 2
            String pId2 = "1260e164-6f2e-43ee-9a53-200ae12aa339";
            String sKey2 = "729c7616-3d32-4117-9238-405694c2cee8";
            String pd2cs1 = "31d4af41-3622-4dbf-9a68-8beed3f79d43";
            String pd2cs2 = "c981e6b7-fa2f-45cb-85ec-59cac3227244";

            // GAMES
            String gId1 = "5a4ee91a-e1a5-4cf3-86a3-dd68d1cdaefa";
            String gId2 = "1b46662e-64ce-432a-8b1c-8a84ca17df25";
            String gId3 = "cb2d2a27-900f-4993-910f-73fc0f07a093";
            String gId4 = "b2db9c80-73d4-412c-be4d-a7e6cd1688d1";

            String gUrl1 = "url1";
            String gUrl2 = "url2";
            String gUrl3 = "url3";
            String gUrl4 = "url4";

            GameCache g1 = new GameCache(gId1, gUrl1);
            GameCache g2 = new GameCache(gId2, gUrl2);
            GameCache g3 = new GameCache(gId3, gUrl3);
            GameCache g4 = new GameCache(gId4, gUrl4);

            // Registering Platforms
            add(pId1, sKey1);
            add(pId2, sKey2);

            // Giving access for platformId + casinoId + gameId
            addGameAccess(pId1, pd1cs1, g1, g2);
            addGameAccess(pId1, pd1cs2, g1, g3);
            addGameAccess(pId2, pd2cs1, g2, g4);
            addGameAccess(pId2, pd2cs2, g1, g2, g4);
            Quarkus.waitForExit();
            return 0;
        }

        private void add(String id, String key) {
            stringCacheRepository.executeIfAbsent(id, AuthCachePrefix.SECRET.getName(), (pid) -> key)
                .await()
                .indefinitely();
        }

        private void addGameAccess(String platformId, String casinoId, GameCache... gameCaches) {
            Arrays.stream(gameCaches).forEach(gameCache -> addGameAccess(platformId, casinoId, gameCache));
        }

        private void addGameAccess(String platformId, String casinoId, GameCache gameCache) {
            String id = RedisKeyHasher.hashCompoundKey(platformId, casinoId, gameCache.getId().toString());
            gameCacheRepository.executeIfAbsent(id, (gid) -> gameCache)
                .await()
                .indefinitely();
        }
    }
}