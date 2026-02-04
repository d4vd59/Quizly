using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Quizly
{
    public class GameState
    {
        public int CurrentRound { get; set; }
        public int TotalRounds { get; set; } = 6;
        public int PlayerScore { get; set; }
        public int OpponentScore { get; set; }
        public string OpponentName { get; set; }
        public bool IsSinglePlayer { get; set; }  // ✅ NEU

        public void Reset()
        {
            CurrentRound = 0;
            PlayerScore = 0;
            OpponentScore = 0;
            OpponentName = string.Empty;
            IsSinglePlayer = false;  // ✅ NEU
        }

        public bool IsGameFinished()
        {
            return CurrentRound >= TotalRounds;
        }
    }
}
