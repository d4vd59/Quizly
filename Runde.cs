using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Quizly
{
    public class Runde
    {
        public int RundeId { get; set; }
        public int RundenNummer { get; set; }

        public Kategorie Kategorie { get; set; }
        public DateTime StartZeit { get; set; }
        public DateTime? EndZeit { get; set; }

        public List<Frage> Fragen { get; set; }

        public void Beenden()
        {
            EndZeit = DateTime.UtcNow;
        }
    }
}