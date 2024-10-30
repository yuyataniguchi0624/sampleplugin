package plugin.sample;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.World;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {

  // BigInteger型の val をクラス変数として定義
  private BigInteger val = new BigInteger("1");
  private URI first;

  @Override
  public void onEnable() {
    Bukkit.getPluginManager().registerEvents(this, this);
  }

  /**
   * プレイヤーがスニークを開始/終了する際に起動されるイベントハンドラ。
   *
   * @param e イベント
   */
  @EventHandler
  public void onPlayerToggleSneak(PlayerToggleSneakEvent e) throws IOException {
    // イベント発生時のプレイヤーやワールドなどの情報を変数に持つ。
    Player player = e.getPlayer();
    World world = player.getWorld();

    List<Color> colorList = List.of(Color.RED, Color.BLUE, Color.WHITE, Color.BLACK);

    // 次の素数を求める
    BigInteger prime = val.nextProbablePrime();
    System.out.println(prime + " が次の素数です。");

    // prime が素数であるかの判定 (確度は 1)
    if (prime.isProbablePrime(1)) {
      System.out.println(prime + " は素数です");
      for (Color color : colorList) {

        // 花火オブジェクトをプレイヤーのロケーション地点に対して出現させる。
        Firework firework = world.spawn(player.getLocation(), Firework.class);

        // 花火オブジェクトが持つメタ情報を取得。
        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        // メタ情報に対して設定を追加
        fireworkMeta.addEffect(
            FireworkEffect.builder()
                .withColor(Color.BLUE)// 青色の花火
                .withColor(Color.YELLOW)
                .with(Type.BALL_LARGE)
                .withFlicker()         // 点滅効果を追加
                .build());
        fireworkMeta.setPower(2 +(2 * 2) / 2); // 花火の飛距離を設定

        // 追加した情報で再設定する。
        firework.setFireworkMeta(fireworkMeta);

        Path path = Path.of("firework.txt");
        Files.writeString(path,"たーまやー", StandardOpenOption.APPEND);
        player.sendMessage(Files.readString(path));
      }
      // 次回スニーク時のために val を次の素数に更新
      val = prime;
    }
  }

   @EventHandler
   public void onPlayerBedEnter(PlayerBedEnterEvent e) {
     Player player = e.getPlayer();
     ItemStack[] itemStacks = player.getInventory().getContents();

     // 64個以下のアイテムを消す
     ItemStack[] filteredItems = Arrays.stream(itemStacks)
         .filter(item -> item != null && (item.getMaxStackSize() != 64 || item.getAmount() == 64))  // Maxスタックサイズが64でないもの、またはスタックが満たされていないものを除外
         .toArray(ItemStack[]::new);  // 新しい配列として戻す

     player.getInventory().setContents(filteredItems);
   }
}

