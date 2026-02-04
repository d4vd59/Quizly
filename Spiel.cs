using System;
using System.Collections.Generic;
using Quizly.Enums;

namespace Quizly
{
    public class Spiel
    {
        public int SpielId { get; set; }
        public Spielmodus Modus { get; set; }
        public Spielstatus Status { get; set; }

        public Spielkonfiguration Konfiguration { get; set; }
        public Schwierigkeitsgrad Schwierigkeitsgrad { get; set; }

        public DateTime StartZeit { get; set; }
        public DateTime? EndZeit { get; set; }

        public List<Spieler> Spieler { get; set; }
        public List<Runde> Runden { get; set; }

        public void Beenden()
        {
            Status = Spielstatus.Beendet;
            EndZeit = DateTime.UtcNow;
        }
    }
}