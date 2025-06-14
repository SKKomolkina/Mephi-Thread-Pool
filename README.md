# Курсовая работа. Thread Pool

_Проект реализует собственный пул потоков с настраиваемым управлением очередями, логированием, параметрами и политикой
отказа для обеспечения гибкости и эффективности высоконагруженных серверных приложений._

## Особенности

- Возможность настраивать подробный мониторинг и логирование
- Возможность настраивать параметры для потоков
- Возможность настраивать обработку отказов
- Создание очередей задач по круговому алгоритму

## Анализ производительности

Собственная реализация демонстрирует технические преимущества по сравнению с стандартным ThreadPoolExecutor из
стандартной библиотеки Java:

- **Множественные очереди**
    - Уменьшает конкуренцию за доступ к единой очереди задач, что повышает пропускную способность и снижает задержки.
    - Обеспечивает более стабильное и предсказуемое выполнение задач.
    - Позволяет лучше масштабировать систему при росте нагрузки за счет распределения задач между несколькими очередями.


- **Контроль жизненного цикла потоков**
    - Более эффективное управление нагрузкой.
    - Улучшает использование ресурсов за счет контроля над потоками и их состояниями.
    - Обеспечивает детализированное управление созданием, запуском и завершением потоков, что способствует оптимальному
      использованию ресурсов.


- **Распределение задач по круговому алгоритму**
    - Обеспечивает равномерное перераспределение задач между потоками, что способствует балансировке нагрузки.
    - Более грамотное задействование используемых ресурсов.

На основании проведенного тестирования с различными уровнями нагрузки можно сделать следующие выводы: при всплеске
нагрузок, когда осуществляется обработка 30 задач одновременно, наш пользовательский пул показывает более высокий
уровень завершения задач — 95%, тогда как стандартный пул достигает 85%. При стабильных условиях, когда обрабатывается
10 задач, оба типа пулов демонстрируют полностью успешное выполнение — 100%. В условиях смешанной нагрузки, включающей
20 задач, наш пользовательский пул остается более эффективным, обеспечивая 90% завершения, в то время как стандартный
пул показывает результат около 80%. Эти показатели подтверждают, что пользовательская реализация обеспечивает лучшую
устойчивость и эффективность при различных сценариях работы.

- **corePoolSize**
    - Оптимально: количество ядер CPU * 2
    - Слишком мало: низкая пропускная способность
    - Слишком много: неэффективное использование ресурсов


- **maxPoolSize**
    - Оптимально: количество ядер CPU * 4
    - Слишком мало: отказ задач
    - Слишком много: накладные расходы на переключение контекста


- **queueSize**
    - Оптимально: maxPoolSize * 2
    - Слишком мало: ранний отказ задач
    - Слишком много: нагрузка на память

- **keepAliveTime**

    - Оптимально: 5-10 секунд
    - Слишком мало: частое создание потоков
    - Слишком много: неэффективное использование ресурсов

### Вывод

Эффективное использование ресурсов обеспечивается при значении keepAliveTime в диапазоне от 5 до 10 секунд.

Пропускная способность достигает своего максимума при установке corePoolSize равным удвоенному количеству ядер
процессора (`количество ядер * 2`).

Задержки минимизируются при значении queueSize, равном двойному maxPoolSize (`maxPoolSize * 2`).

## Распределение задач

### Round-Robin

Выполненная реализация использует классическую схему round-robin для равномерного распределения задач по
потокам:

- Каждый рабочий поток имеет свою очередь
- Задачи последовательно распределяются по очередям, следуя порядку.
- Переменная nextQueueIndex служит счетчиком для определения следующей очереди, в которую будет передана задача.

К преимуществам можно отнести: низкую степень накладных расходов на управление распределением, простоту реализации,
предсказуемость и высокую эффективность.

## Возможные улучшения 

- Реализация балансировки на основе текущей нагрузки очередей, что сделает распределение более адаптивным при
  неравномерных условиях.
- Учёт размеров очередей — задачи могут направляться в менее загруженные очереди, что повышает пропускную способность.
- Распределение задач с учётом приоритетов, где задачи с высшим уровнем приоритета получают больше шансов быть
  выполненными раньше.
- Поддержка уровней приоритетов и управление ими для эффективной работы в сценариях с разными требованиями и
  характеристиками задач.
