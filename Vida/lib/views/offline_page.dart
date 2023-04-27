import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:ionicons/ionicons.dart';
import 'package:on_audio_query/on_audio_query.dart';
import 'package:Vida/consts/colors.dart';
import 'package:Vida/consts/text_style.dart';
import 'package:Vida/controllers/player_controller.dart';
import 'package:Vida/views/player.dart';
import 'package:Vida/widget/loved_icon.dart';
import '../widget/custom_icon_button.dart';

class OfflinePage extends StatefulWidget {
  OfflinePage({super.key});

  @override
  State<OfflinePage> createState() => _OfflinePageState();
}

class _OfflinePageState extends State<OfflinePage> {
  var controller = Get.put(PlayerController());
  Future refresh() async {
    setState(() {});
  }

  List<Widget> buildTrailing(int index, bool isPlaying) {
    print("Building");
    LovedIcon icon = LovedIcon(isLoved: controller.isLoveds[index]);
    var widgets = <Widget>[];
    widgets.add(isPlaying
        ? Icon(
            Icons.play_arrow,
            color: purpButton,
            size: 32.0,
          )
        : Padding(
            padding: const EdgeInsets.all(16),
          ));
    widgets.add(
      IconButton(
          onPressed: () {
            controller.isLoveds[index].value =
                !controller.isLoveds[index].value;
          },
          icon: icon),
    );

    return widgets;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: blackBG,
      appBar: AppBar(
        elevation: 0,
        backgroundColor: blackBG,
        foregroundColor: littleWhite,
        title: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const SizedBox(height: 5),
            Row(
              children: [
                Icon(Ionicons.headset, color: purpButton, size: 40),
                SizedBox(
                  width: 5,
                ),
                Padding(
                  padding: const EdgeInsets.only(top: 9),
                  child: Text(
                    "Vida",
                    style: TextStyle(
                        color: flutterPurple,
                        fontSize: 25,
                        fontWeight: FontWeight.bold,
                        fontFamily: 'Comfortaa'),
                  ),
                ),
              ],
            ),
          ],
        ),
        actions: [
          CustomIconButton(
            icon: Icon(
              size: 25,
              Icons.search,
              color: flutterPurple,
            ),
          ),
        ],
      ),
      body: RefreshIndicator(
        onRefresh: refresh,
        backgroundColor: purpButton,
        color: white,
        strokeWidth: 4,
        displacement: 50,
        child: FutureBuilder<List<SongModel>>(
            future: controller.audioQuery.querySongs(
                ignoreCase: true,
                orderType: OrderType.ASC_OR_SMALLER,
                sortType: null,
                uriType: UriType.EXTERNAL),
            builder: (BuildContext context, snapshot) {
              //String searchValue = '';
              if (controller.isLoveds.isEmpty)
                controller.isLoveds = List.generate(
                    snapshot.data?.length ?? 0, (index) => RxBool(false));
              //<RxBool>[snapshot.data?.length];
              List<String> listTitle = List.generate(snapshot.data?.length ?? 0,
                  (index) => snapshot.data![index].title);
              print("Debug list view");
              if (snapshot.data == null) {
                return const Center(
                    child: CircularProgressIndicator(
                  backgroundColor: Colors.amberAccent,
                ));
              } else if (snapshot.data!.isEmpty) {
                print(
                    "~~~~~~~~~~~~~~~data is empty bro ${snapshot.data}~~~~~~~~~~~~~~~");
                return Center(
                    child: Text("No song found in local storage",
                        style: ourStyle()));
              } else {
                print(
                    "~~~~~~~~~~~~~~~data is GOOD bro ${snapshot.data}~~~~~~~~~~~~~~~");

                return Padding(
                  padding: const EdgeInsets.all(10),
                  child: ListView.builder(
                      physics: BouncingScrollPhysics(
                          parent: AlwaysScrollableScrollPhysics()),
                      itemCount: snapshot.data!.length,
                      itemBuilder: (BuildContext context, int index) {
                        return Container(
                          margin: const EdgeInsets.only(bottom: 8),
                          child: Obx(
                            () => ListTile(
                              shape: RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(13.0)),
                              tileColor: blackTextFild,
                              title: Text(snapshot.data![index].title,
                                  style: ourStyle(
                                    color: white,
                                    fontWeight: FontWeight.bold,
                                    size: 15.0,
                                  )),
                              subtitle: Text("${snapshot.data![index].artist}",
                                  style:
                                      ourStyle(size: 15.0, color: littleWhite)),
                              leading: Container(
                                padding: const EdgeInsets.all(1.0),
                                decoration: BoxDecoration(
                                    borderRadius: BorderRadius.circular(50),
                                    color: purpButton),
                                child: QueryArtworkWidget(
                                  artworkQuality: FilterQuality.high,
                                  quality: 100,
                                  keepOldArtwork: true,
                                  id: snapshot.data![index].id,
                                  type: ArtworkType.AUDIO,
                                  nullArtworkWidget: const Icon(
                                    Icons.music_note,
                                    color: whiteColor,
                                    size: 32,
                                  ),
                                ),
                              ),
                              trailing: SizedBox(
                                width: 86,
                                child: Row(
                                  children: buildTrailing(
                                      index,
                                      controller.playIndex.value == index &&
                                          controller.isPlaying.value),
                                ),
                              ),
                              onTap: () {
                                Get.to(
                                  () => Player(songList: snapshot.data!),
                                  transition: Transition.downToUp,
                                );
                                controller.playSong(
                                    snapshot.data![index].uri, index);
                              },
                            ),
                          ),
                        );
                      }),
                );
              }
            }),
      ),
    );
  }
}