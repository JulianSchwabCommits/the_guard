# 🎮 The Guard - Quick Reference Card

**Schnelle Übersicht für Spieler**

---

## 🚀 Schnellstart

```bash
# Kompilieren
javac -d target/classes src/main/java/ch/bbw/zork/*.java

# Spielen
cd target/classes
java ch.bbw.zork.GuardGame
```

---

## 🎯 Befehle

| Befehl | Was es tut | Beispiel |
|--------|------------|----------|
| `help` | Hilfe anzeigen | `help` |
| `go <richtung>` | Bewegen | `go north` |
| `search <ort>` | Durchsuchen | `search desk` |
| `hide` | Verstecken | `hide` |
| `inventory` | Inventar | `inventory` |
| `use <item>` | Item nutzen | `use key` |
| `next` | Warten (versteckt) | `next` |
| `quit` | Beenden | `quit` |

**Richtungen:** `north`, `south`, `east`, `west`

---

## 🏆 3 Wege zum Sieg

### ⭐ Route 1: Schlüssel (5 min)
```
search desk → go north → go east → search desk 
→ go west → go south → go east → go south 
→ use schlüssel
```

### ⭐⭐ Route 2: Wächter (10 min)
```
go west → go south → go south → go east 
→ search cabinets → hide → next → next 
→ use messer
```

### ⭐⭐⭐ Route 3: Teleporter (15 min)
```
go west → go south → go south → go west 
→ search chest → use teleporter
```

---

## 🗺️ Wichtige Räume

| Raum | Was ist dort | Wie kommt man hin |
|------|--------------|-------------------|
| **Büro Chef** | 🔑 Schlüssel | north → east |
| **Küche** | 🔪 Messer | west → south → south → east |
| **Versteck** | ✨ Teleporter | west → south → south → west |
| **Ausgang** | 🚪 Freiheit | east → south |

---

## ⚡ Regeln

✅ **2 Aktionen pro Runde**  
✅ **Nur 1x bewegen pro Runde**  
✅ **Versteckt = Wächter sieht dich nicht**  
❌ **Wächter + du im Raum + nicht versteckt = GAME OVER**

---

## 💡 Pro-Tipps

🫥 **Wenn Wächter kommt:** `hide` → `next`  
🔍 **Alles durchsuchen:** Items sind versteckt!  
📦 **Inventar prüfen:** `inventory` oft nutzen  
🗺️ **Orientierung:** Empfangshalle = Zentrum

---

## 🚨 Notfall

**Verloren?** → Empfangshalle ist Start (north/south/east/west)  
**Keine Aktionen?** → Warte bis nächste Runde  
**Erwischt?** → GAME OVER → Neustart  
**Stuck?** → Check WALKTHROUGH.md

---

## 📊 Items-Übersicht

| Item | Typ | Fundort | Zweck |
|------|-----|---------|-------|
| Notiz | HINT | Empfangshalle (desk) | Hinweis |
| Schlüssel | KEY | Büro Chef (desk) | Entkommen |
| Messer | WEAPON | Küche (cabinets) | Wächter |
| Teleporter | TELEPORTER | Versteck (chest) | Easter Egg |
| Taschenlampe | TOOL | Keller (shelves) | - |

---

## 🎮 Erste Schritte

1. `search desk` - Finde Hinweis
2. `inventory` - Check Items
3. `go north` - Geh zu Flur OG
4. `go east` - Geh zu Büro
5. `search desk` - SCHLÜSSEL!
6. `use schlüssel` (beim Ausgang) - GEWONNEN!

---

**Viel Erfolg! 🎉**

*Siehe README.md für Details | WALKTHROUGH.md für komplette Lösungen*
