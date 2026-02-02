using System.Windows.Controls;

namespace Quizly.Views
{
    public partial class MatchResultView : UserControl
    {
        private readonly MainWindow _main;

        public MatchResultView(MainWindow main)
        {
            InitializeComponent();
            _main = main;
        }

        private void Menu_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.NavigateTo(new StartView(_main));
        }

        private void Rematch_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.NavigateTo(new MatchmakingView(_main));
        }
    }
}
