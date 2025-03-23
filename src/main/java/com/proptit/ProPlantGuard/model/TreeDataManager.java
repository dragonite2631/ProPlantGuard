package com.proptit.ProPlantGuard.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TreeDataManager {
    private static final String FILE_NAME = "trees.json";

    public static void saveTree(Tree tree) {
        List<Tree> treeList = loadExistingTrees();
        int index = treeList.indexOf(tree);
        if (index != -1) {
            treeList.set(index, tree);
            saveTreesToJson(treeList);
        } else {
            System.out.println("Tree not found in the list.");
        }
    }

    public static void saveTreeToJson(Tree tree) {
        List<Tree> treeList = loadExistingTrees();
        int index = treeList.indexOf(tree);
        if (index != -1) {
            treeList.set(index, tree);
        } else {
            treeList.add(tree);
        }
        saveTreesToJson(treeList);
    }
    public static List<Tree> loadExistingTrees() {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(FILE_NAME);

        if (file.exists()) {
            try {
                CollectionType listType = objectMapper.getTypeFactory()
                        .constructCollectionType(ArrayList.class, Tree.class);
                return objectMapper.readValue(file, listType);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error while loading trees from JSON file.");
            }
        }

        return new ArrayList<>();
    }

    public static void saveTreesToJson(List<Tree> treeList) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT);
        try {
            File file = new File(FILE_NAME);
            objectMapper.writeValue(file, treeList);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while saving trees to JSON file.");
        }
    }
}
