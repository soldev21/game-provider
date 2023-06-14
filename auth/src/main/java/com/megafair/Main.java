//package com.megafair;
//
//import com.megafair.cache.GameCacheRepository;
//import com.megafair.cache.RedisKeyHasher;
//import com.megafair.cache.model.GameCache;
//import io.quarkus.runtime.Quarkus;
//import io.quarkus.runtime.QuarkusApplication;
//import io.quarkus.runtime.annotations.QuarkusMain;
//import jakarta.inject.Inject;
//
//import java.util.UUID;
//
//@QuarkusMain
//public class Main {
//    public static void main(String... args) {
//        Quarkus.run(MyApp.class, args);
//    }
//
//    public static class MyApp implements QuarkusApplication {
//
//        @Inject
//        GameCacheRepository gameCacheRepository;
//
//        @Override
//        public int run(String... args) throws Exception {
//            String pId = "c8ef876f-8a9b-4838-b108-e4b4bfa85d23";
//            String cId = "3beecaaa-e681-42e1-bd64-f0afb9f04e79";
//            String gId = "2aaeb0de-1c1c-42a7-8a2d-e4a4ba7bcf6e";
//            gameCacheRepository.executeIfAbsent(RedisKeyHasher.hashCompoundKey(pId, cId, gId), (id) -> new GameCache(UUID.fromString(gId), "someUrl")).
//            await().indefinitely();
//            System.out.println("Do startup logic here");
//            Quarkus.waitForExit();
//            return 0;
//        }
//    }
//}