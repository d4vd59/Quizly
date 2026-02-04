using System.Windows;
using System.Windows.Controls;

namespace Quizly.Views
{
    public partial class ResultView : UserControl
    {
        private readonly MainWindow _main;

        public ResultView(MainWindow main)
        {
            //InitializeComponent(); 
            _main = main;
        }

        private void Back_Click(object sender, RoutedEventArgs e)
        {
            _main.NavigateTo(new DuelOverviewView(_main));
        }

        private void PlayAgain_Click(object sender, RoutedEventArgs e)
        {
            _main.NavigateTo(new CategoryPickView(_main));
        }
    }
}