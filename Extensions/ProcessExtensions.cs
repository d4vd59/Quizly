using System;
using System.Diagnostics;
using System.Threading;
using System.Threading.Tasks;

namespace Quizly.Extensions
{
    public static class ProcessExtensions
    {
        public static Task WaitForExitAsync(this Process process, CancellationToken cancellationToken = default)
        {
            if (process == null) throw new ArgumentNullException(nameof(process));
            if (process.HasExited) return Task.CompletedTask;

            var tcs = new TaskCompletionSource<bool>();

            void Handler(object s, EventArgs e)
            {
                process.Exited -= Handler;
                tcs.TrySetResult(true);
            }

            process.EnableRaisingEvents = true;
            process.Exited += Handler;

            if (cancellationToken != default)
            {
                cancellationToken.Register(() =>
                {
                    process.Exited -= Handler;
                    tcs.TrySetCanceled();
                });
            }

            return tcs.Task;
        }
    }
}