# Project GlobalWaves - Pagination
This project involves the implementation of an application with features akin to Spotify.
## Author
Ana Ailiesei
## Project structure
- [audio](#audio) - Implementations for audio files
- [commands](#commands) - Implementation for all the commands
- [entities](#entities) - Implementations regarding all app entities
- [fileio](#fileio) - Classes involved in input output operations
- [libraries](#libraries) - Libraries for all the entities
- [managers](#managers) - Classes used for managing different commands
- [notifications](#notifications) - Classes used for notifications
- [playables](#playables) - Classes for playable entities
- [profile](#profile) - Profile related objects for hosts and artists
- [recommendation](#recommendation) - Implementation for recommendations
- [statistics](#statistics) - Keeping track and calculating statistics
- [main](#main) - The main file

## Design patterns
- Strategy - Used for revenue calculations [here](#StrategyPattern)
- Command - Used for executing the change page commands (`previousPage` and `nextPage`) [here](#CommandPattern)
- Observer - Used for the notifications and time change systems [here](#ObserverPattern) and [here](#Listener)
- Factory -  Command execution orchestrator, generates managers to handle commands [here](#FactoryPattern)
- Singleton - Used for libraries and manager classes [here](#SingletonPattern1) and [here](#SingletonPattern2)
## commands
### admin
- Static admin commands like `AddUser`, `DeleteUser`, `AdBreak`, `AddPremiumUser`, `RemovePremiumUseer`, and `End`
- Executed through the `AdminCommandsManager`.
### artist
- Static artist commands like `AddAnnouncement`, `AddPodcast`, `RemoveAnnouncement`, and `RemovePodcast`.
- Executed through the `ArtistCommandsManager`.
### host
- Static host commands like `AddAnnouncement`, `AddPodcast`, `RemoveAnnouncement`, and `RemovePodcast`
- Executed through the `HostCommandsManager`
### normalUser
#### general
- General static commands like `BuyMerch`, `Subscribe` and `SwitchConnectionStatus`
- Executed through the `PurchaseManager` and `ConnectionStatusManager`, respectively
#### pageNavigation
Pagination specific classes such as:
- `ChangePageUser` - the page changing logic is here and is used as a helper
- `ChangePage` - that uses the invoker to change pages
- `Page` - record that contains information about pages such as its type and owner
- `PageChnageInvoker` - this is used for managing change page commands, and it's implemented using the <span style="color:pink;" id="CommandPattern">Command pattern</span>
- `PageType` - for all possible page types 

Executed through the `PageSystemManager`
#### player
- Commands related to the player are structured using the Command pattern. 
This design choice originated in the project's initial stages, and it has been retained in 
its current form. The decision to maintain this pattern is rooted in the anticipation of 
potential future tasks that may involve undo operations, requiring the use of a command stack (which is absent for now).
- The commands are `AddRemoveInPlaylist`, `Backward`, `Forward`, `Like`, `LoadAudio`, `LoadRecommendation`, `Load` (helper), `Next`, `PlayPause`, `Prev`, `Repeat`, and
`Shuffle`
- A single instance for each command is kept using the `CommandManager` for each `AppManager`.
- Executed trough `PlayerManager`
#### playlist
- Static commands related to playlists such as `CreatePlaylist`, `FollowPlaylist`, and `SwitchVisibility`
- Executed trough the `PlaylistManager`
#### searchBar
- Commands related to the search bar are structured using the Command pattern, with the same reasoning as before. 
- The search and select component of the app is divided into two categories: `SearchAudio`/`SelectAudio` for audio entities and
  `SearchUser`/`SelectUser` for user entities, with a common `Search`/`Select` abstract class for generality.
- Each filter class implements the initialization for the mapping between the filtering methods used and the corresponding
filters, ensuring a unified implementation for search and filtering, regardless of the search type.
- Executed through the `SearchBarManager`
## entities
Implementation for all entities in this app (users or audio files). Common entity interface. The`NameableEntity` class provides a base implementation for entities that have a name. It implements the CommonEntity interface and includes a `getName()` method.
### audio
Implementation for audio file classes and collections.
- Contains a generic `Audio` interface for all audio entities, including `Song` and `Episode` (audio files).
#### collections
- Manages collections such as Albums, Podcasts, and Playlists.
### user
User implementations:
- `NormalUser`: Represents a regular user.
- `Host`: Represents a host user.
- `Artist`: Represents an artist user.
## fileio
- All classes are dedicated to reading from and writing to `.json` files.
- The `Output` class is particularly important, as the primary class for handling output in nearly all commands.
## libraries
- Dedicated classes for storing information about app entities along with common methods like adding and removing.
- The libraries are:
  - For audio: `AlbumsLibrary`, `PlaylistsLibrary`, `PodcastsLibrary`, `SongsLibrary`
  - For users: `ArtistsLibrary`, `HostsLibrary`, `NormalUsersLibrary`

Used a <span style="color:pink;" id="SingletonPattern1">Singleton pattern</span>
## managers
- Already iterated through most managers:
    - `AdminCommandsManager` for admin commands.
    - `ArtistCommandsManager` for artist commands.
    - `HostCommandsManager` for host commands.
    - `AppManager` for all the user related operations.
    - `CommandManager`, `ConnectionStatusManager`,`PageSystemManager`, `PlayerManager`, `PlaylistManager`, `PurchaseManager`,`SearchBarManager`
  for normal user related commands.
    - `ProgressManager` manages the in-progress tracks for a user.

- The `NotificationsManager` employs the <span style="color:pink;" id="ObserverPattern">Observer design pattern</span> 
to oversee and handle notifications. When a content creator triggers a new event, all subscribed users receive notifications.
- `GeneralStatisticsManager` for commands like `Top5Playlists`, `Top5Songs`, `GetOnlineUsers`, `GetAllUsers`, `GetTop5Albums`, and `GetTop5Artists`, utilizing operations with streams
- `TimeManager`: Implemented using the  <span style="color:pink;" id="Listener">Observer pattern</span> for time changes. 
This class notifies all listeners when a time change occurs. The observers are the `PlayerManager` instances within each user's app.
- `CommandManagerFactory`: Utility class responsible for creating instances of `CommandHandler` based on the specified 
`CommandType` and and the username of the user performing the command. It follows the <span style="color:pink;" id="FactoryPattern">Factory design pattern</span>. It optimizes the 
execution of commands by dynamically creating instances of `CommandHandler` (managers) based on the provided command.
- Added exception handling that may occur during the manager instance creation process and command execution.
- The managers are created using the <span style="color:pink;" id="SingletonPattern2">Singleton pattern</span> with lazy synchronized instantiation.
## notifications
Notifications related classes:
- `Notifier` - interface defines a contract for classes that handle the delivery of notifications. Classes implementing
this interface act as central hubs for notifying subscribers about various events.
- `Notifiable` - defines a contract that entities can implement to become eligible for notification. 
Classes that implement this interface can be registered with the `Notifier` to receive notifications.
- `Notification` - utility class for notification generation.
- `NotificationType` - defines different types of notifications.

## playables
Implementations for playable entities that should be loaded by the player.
- `PlayingAudio` for audio files (`Song` and `Episode`).
- `PlayingCollection` for audio collections (`Podcast`, `Playlist`, `Album`).
## profile
Aditional entities used for profiles
- `Artist` profile
  - `Event`: Represents events associated with an artist.
  - `Merch`: Represents merchandise associated with an artist.
  - `DateValidation`: Utility class for validating event dates.
- `Host` profile
  - `Announcement`: Represents announcements made by a host.
## recommendation
`Recommendation` is a utility class with a set of methods for generating diverse recommendations, including random songs, playlists, and fan suggestions.
## statistics
Implementations for managing statistics
### calculator
Implementations for calculating the revenue. Used the <span style="color:pink;" id="StrategyPattern">Strategy pattern</span>
- `CalculateRevenueStrategy` is the blueprint for revenue calculation strategies.  Classes implementing 
this interface provide specific algorithms for calculating revenue based on various criteria.
- `RevenueCalculator` class serves as the orchestrator, utilizing different strategies to calculate 
revenue. It delegates the task to classes implementing the `CalculateRevenueStrategy` interface.

- `ArtistsCalculateRevenue`
  - Implements the `CalculateRevenueStrategy` interface.
  - Provides a strategy for calculating revenue based on the revenue made by the artist's songs.

- `ArtistsCalculateRevenueAlbum`
  - Implements the `CalculateRevenueStrategy` interface.
  - Provides a strategy for calculating revenue based on the revenue made by the artist's songs in the given album.
- `FreeSongCalculateRevenue`
  - Implements the `CalculateRevenueStrategy` interface.
  - Offers a strategy for monetization of free songs.

- `PremiumSongCalculateRevenue`
  - Implements the `CalculateRevenueStrategy` interface.
  - Defines a strategy for monetization tailored to premium songs.
### listenTrackers
Track listens for various entities.

- `ListenTracker` class, with a generic type of entity, is responsible for counting the number of listens a particular entity has received.
- `ListenTrackerArtist` extends the functionality of `ListenTracker` and holds multiple `ListenTracker`s. It is specifically tailored for tracking artist statistics.
- `ListenTrackerHost` class specializes in holding multiple `ListenTracker`s, focusing on host-related statistics.
- `ListenTrackerNormalUser` is another extension of `ListenTracker` designed for tracking listens pertaining to normal users.

## main
- Utilizes the `CommandManagerFactory` for command execution.
- Implements methods for resetting libraries between use cases.