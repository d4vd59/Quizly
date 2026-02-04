using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Quizly
{
    public class Frage
    {
        public int Id { get; set; }
        public string Text { get; set; }

        public Kategorie Kategorie { get; set; }
        public Schwierigkeitsgrad Schwierigkeitsgrad { get; set; }

        public int RichtigBeantwortet { get; set; }
        public int FalschBeantwortet { get; set; }

        public List<Antwort> Antworten { get; set; }

        public bool IstAntwortRichtig(int antwortId)
        {
            return Antworten.Any(a => a.Id == antwortId && a.IstRichtig);
        }
    }
}