package org.saveload;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.model.board.MoveResolver;
import org.model.match.Match;
import org.model.board.RockSpawner;
import org.model.cards.Card;
import org.model.entities.Entity;


/**
 * <p>Dieses Interface wird zur Implementierung von persistenten Speicherungstechniken für die {@link Match} Speicherung verwendet.</p>
 *
 * <p>Dieses Interface ist Teil des <strong>Dependency Injection Entwurfsmusters</strong>: Methodenaufrufe zum Laden
 * und Speichern ("Clients") geschehen durch Übergabe eines beliebigen Objekts ("Injector"), das dieses Interface implementiert. Somit
 * ist das Speichern und Laden nicht an die eigentliche Implementierung dieser Funktionen gekoppelt und kann
 * durch beliebige Konfigurationen umgesetzt werden.</p>
 *
 * <p>Zur Veranschaulichung sind hier zwei konkrete Injektoren implementiert: <br>
 * {@link SlotSave} - Speichern und Laden aus voreingestellten Speicherslots <br>
 * {@link FileSave} - Speichern und Laden aus einer externen Datei</p>
 */
public interface Persistence {

  boolean save(String identifier, Match match);

  Match load(String identifier);

    /**
     * @return GSON-Objekt für das Handling von JSON-Speicherdateien
     */
  default Gson getGson()
  {
      return new GsonBuilder()
              .enableComplexMapKeySerialization()
              .setPrettyPrinting()
              .registerTypeAdapter(Card.class, new PolymorphicDeSerializer<Card>())
              .registerTypeAdapter(Entity.class, new PolymorphicDeSerializer<Entity>())
              .registerTypeAdapter(RockSpawner.class, new PolymorphicDeSerializer<RockSpawner>())
              .registerTypeAdapter(MoveResolver.class, new PolymorphicDeSerializer<MoveResolver>())
              .create();
  }



}
