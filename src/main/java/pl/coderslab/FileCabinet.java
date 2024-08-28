package pl.coderslab;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileCabinet implements Cabinet {
    private List<Folder> folders;

    public FileCabinet(List<Folder> folders) {
        this.folders = folders;

    }

    @Override
    public Optional<Folder> findFolderByName(String name) {
        return findFolderByName(folders, name);
    }

    private Optional<Folder> findFolderByName(List<Folder> folders, String name) {
        return folders.stream()
                .flatMap(folder -> folder instanceof MultiFolder ?
                        Stream.concat(Stream.of(folder), ((MultiFolder) folder).getFolders().stream()) :
                        Stream.of(folder))
                .filter(folder -> folder.getName().equals(name))
                .findFirst();
    }


    @Override
    public List<Folder> findFoldersBySize(String size) {
        return findFoldersBySize(folders, size);
    }

    private List<Folder> findFoldersBySize(List<Folder> folders, String size) {
        return folders.stream()
                .flatMap(folder -> folder instanceof MultiFolder ?
                        Stream.concat(Stream.of(folder), ((MultiFolder) folder).getFolders().stream()) :
                        Stream.of(folder))
                .filter(folder -> folder.getSize().equals(size))
                .collect(Collectors.toList());
    }

    @Override
    public int count() {
        return count(folders);
    }

    private int count(List<Folder> folders) {
        return folders.stream()
                .mapToInt(folder -> folder instanceof MultiFolder ? 1 + count(((MultiFolder) folder).getFolders()) : 1)
                .sum();
    }

    interface Folder {
        String getName();

        String getSize();
    }

    interface MultiFolder extends Folder {
        List<Folder> getFolders();
    }
}