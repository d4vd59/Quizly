using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Quizly
{
    public class Spieler
    {
        public int UserId { get; set; }
        public string Nickname { get; set; }
        public bool EmailConfirmed { get; set; }
        public DateTime? LastLogin { get; set; }

        public int GesamtPunkte { get; set; }

        public void AddPunkte(int punkte)
        {
            GesamtPunkte += punkte;
        }
    }
}