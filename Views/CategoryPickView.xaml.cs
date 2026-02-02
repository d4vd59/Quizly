using System.Windows.Controls;

namespace Quizly.Views
{
    public partial class CategoryPickView : UserControl
    {
        private readonly MainWindow _main;

        public CategoryPickView(MainWindow main)
        {
            InitializeComponent();
            _main = main;
        }

        private void Back_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.NavigateTo(new DuelOverviewView(_main));
        }

        private void Continue_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.NavigateTo(new QuestionView(_main));
        }
    }
}
