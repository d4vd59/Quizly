using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Quizly
{
    public class Antwort
    {
        public int Id { get; set; }
        public string Text { get; set; }
        public bool IstRichtig { get; set; }
    }
}