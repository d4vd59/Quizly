using System.Windows.Controls;

namespace Quizly.Views
{
    public partial class QuestionView : UserControl
    {
        private readonly MainWindow _main;

        public QuestionView(MainWindow main)
        {
            InitializeComponent();
            _main = main;
        }

        private void Back_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.NavigateTo(new CategoryPickView(_main));
        }

        private void Answer_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.NavigateTo(new ResultView(_main));
        }
    }
}
