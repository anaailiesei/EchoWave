package main;

import entities.audio.Song;
import entities.audio.collections.Podcast;
import checker.Checker;
import checker.CheckerConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.CommandType;
import commands.admin.End;
import fileio.input.CommandInput;
import fileio.input.LibraryInput;
import fileio.output.Output;
import fileio.output.PageOutput;
import libraries.audio.AlbumsLibrary;
import libraries.audio.PlaylistsLibrary;
import libraries.audio.PodcastsLibrary;
import libraries.audio.SongsLibrary;
import libraries.users.ArtistsLibrary;
import libraries.users.HostsLibrary;
import libraries.users.NormalUsersLibrary;
import managers.TimeManager;
import managers.commands.CommandHandler;
import managers.commands.CommandManagerFactory;
import entities.user.NormalUser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    static final String LIBRARY_PATH = CheckerConstants.TESTS_PATH + "library/library.json";

    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     *
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.getName().startsWith("library")) {
                continue;
            }

            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }
        // test00_etapa3_wrapped_one_user_one_artist.json

//        String fileName = "test01_etapa3_wrapped_one_user_n_artist.json";
//        String filepath = CheckerConstants.OUT_PATH + fileName;
//        File out = new File(filepath);
//        boolean isCreated = out.createNewFile();
//        if (isCreated) {
//            action(fileName, filepath);
//        }

        Checker.calculateScore();
    }

    /**
     * @param filePathInput  for input file
     * @param filePathOutput for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePathInput,
                              final String filePathOutput) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        LibraryInput library = objectMapper.readValue(new File(LIBRARY_PATH), LibraryInput.class);

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        ArrayNode outputs = objectMapper.createArrayNode();

        resetLibraries(library);

        TypeReference<ArrayList<CommandInput>> typeRef = new TypeReference<>() {
        };
        ArrayList<CommandInput> commands = objectMapper
                .readValue(new File("input/" + filePathInput), typeRef);


        ObjectNode outNode;
        for (CommandInput command : commands) {
            TimeManager.getInstance().setTime(command.getTimestamp());
            CommandType commandType = command.getCommand();
            String username = command.getUsername();

            if (commandType.equals(CommandType.printCurrentPage)) {
                NormalUser user = NormalUsersLibrary.getInstance().getUserByName(username);
                assert user != null;
                PageOutput pageOut = user.performPrintCurrentPage(command);
                outNode = objectMapper.valueToTree(pageOut);
                outputs.add(outNode);
            } else {
                CommandHandler commandHandler = CommandManagerFactory
                        .createManager(commandType, username);
                if (commandHandler != null) {
                    Output out = commandHandler.performCommand(command);
                    outNode = objectMapper.valueToTree(out);
                    outputs.add(outNode);
                }
            }
        }

        Output endProgram = End.performEndProgram();
        outNode = objectMapper.valueToTree(endProgram);
        outputs.add(outNode);
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePathOutput), outputs);
    }

    /**
     * Resets or sets the libraries in the application.
     *
     * @param library The source library containing initial data.
     */
    public static void resetLibraries(final LibraryInput library) {
        resetSongs(library);
        resetPodcasts(library);
        resetPlaylists();
        resetUsers(library);
        resetAlbums();
        resetArtists();
        resetHosts();
    }

    /**
     * Resets the SongsLibrary by creating new Song instances based on the input library.
     *
     * @param library The source library containing initial song data.
     */
    private static void resetSongs(final LibraryInput library) {
        ArrayList<Song> songs = new ArrayList<>();
        library.getSongs().forEach(songInput -> songs.add(new Song(songInput)));
        SongsLibrary.getInstance().setItems(songs);
    }

    /**
     * Resets the PodcastsLibrary by creating new Podcast instances based on the input library.
     *
     * @param library The source library containing initial podcast data.
     */
    private static void resetPodcasts(final LibraryInput library) {
        ArrayList<Podcast> podcasts = new ArrayList<>();
        library.getPodcasts().forEach(podcastInput -> podcasts.add(new Podcast(podcastInput)));
        PodcastsLibrary.getInstance().setItems(podcasts);
    }

    /**
     * Resets the PlaylistsLibrary by creating an empty ArrayList.
     */
    private static void resetPlaylists() {
        PlaylistsLibrary.getInstance().setItems(new ArrayList<>());
    }

    /**
     * Resets the NormalUsersLibrary by creating new NormalUser instances
     * based on the input library.
     *
     * @param library The source library containing initial entities.user data.
     */
    private static void resetUsers(final LibraryInput library) {
        ArrayList<NormalUser> users = new ArrayList<>();
        library.getUsers().forEach(userInput -> users.add(new NormalUser(userInput)));
        NormalUsersLibrary.getInstance().setItems(users);
    }

    /**
     * Resets the AlbumsLibrary by creating an empty ArrayList.
     */
    private static void resetAlbums() {
        AlbumsLibrary.getInstance().setItems(new ArrayList<>());
    }

    /**
     * Resets the ArtistsLibrary by creating an empty ArrayList.
     */
    private static void resetArtists() {
        ArtistsLibrary.getInstance().setItems(new ArrayList<>());
    }

    /**
     * Resets the HostsLibrary by creating an empty ArrayList.
     */
    private static void resetHosts() {
        HostsLibrary.getInstance().setItems(new ArrayList<>());
    }
}
