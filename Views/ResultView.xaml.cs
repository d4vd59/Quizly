using Quizly.Views;
using System.Windows.Controls;

namespace Quizly.Views
{
    public partial class ResultView : UserControl
    {
        private readonly MainWindow _main;

        public ResultView(MainWindow main)
        {
            InitializeComponent();
            _main = main;
        }

        private void Back_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.NavigateTo(new StartView(_main));
        }

        private void Next_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.NavigateTo(new QuestionView(_main));
        }

        private void MatchEnd_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.NavigateTo(new MatchResultView(_main));
        }
    }
}
