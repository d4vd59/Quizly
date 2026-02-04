using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Quizly
{
    public class Spielkonfiguration
    {
        public int ConfigId { get; set; }
        public string ConfigName { get; set; }

        public int AnzahlRunden { get; set; }
        public int FragenProRunde { get; set; }
        public int AntwortenProFrage { get; set; }

        public int MaxWiederholungenFrage { get; set; }
        public int MaxSpiellaengeSekunden { get; set; }

        public bool IstAktiv { get; set; }
    }
}