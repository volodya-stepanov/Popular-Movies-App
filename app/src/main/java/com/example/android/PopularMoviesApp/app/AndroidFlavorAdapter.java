package com.example.android.PopularMoviesApp.app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Володя on 10.10.2016.
 */
public class AndroidFlavorAdapter extends ArrayAdapter<AndroidFlavor> {
    private static final String LOG_TAG = AndroidFlavorAdapter.class.getSimpleName();

    /**
     * Это наш собственный конструктор (он не отражает конструктор суперкласса).
     * Контекст используется, чтобы наполнить файл макета, а List - это данные, которыми мы хотим
     * наполнить списки
     *
     * @param context           Текущий контекст. Используется, чтобы заполнить файл макета
     * @param androidFlavors    Список объектов класса AndroidFlavor для отображения в списке
     */
    public AndroidFlavorAdapter(Activity context, List<AndroidFlavor> androidFlavors)
    {
        // Здесь мы инициализируем внутреннее хранилище объектов ArrayAdapter для контекста и списка.
        // Второй аргумент используется, когда ArrayAdapter наполняет одиночное TextView.
        // Поскольку это пользовательский адаптер для двух объектов TextView и ImageView, адаптер не
        // будет использовать этот второй аргумент, поэтому он может быть любым значением. Здесь мы использовали 0.
        super(context, 0, androidFlavors);
    }

    /**
     * Предоставляет элемент view для AdapterView (ListView, GridView и т.д.)

     * @param position      Позиция элемента AdapterView, которая запрашивает view
     * @param convertView   Рециклированное view, которое необходимо наполнить
     *                      (см. "android view recycling" в интернете, чтобы узнать больше)
     * @param parent        Родительская группа view (ViewGroup), используемая для заполнения
     * @return              View для позиции в AdapterView
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Получает объект AndroidFlavor из ArrayAdapterа на указанной позиции
        AndroidFlavor androidFlavor = getItem(position);

        // Адаптеры рециркулируют элементы view элементам AdapterView
        // Если это новый элемент view, который мы получаем, то мы заполняем макет
        // Если нет, этот элемент view уже имеет макет, заполненный из предыдущего вызова метода getView
        // и мы модифицируем виджеты view как обычно
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.grid_item_layout, parent, false);
        }

        ImageView iconView = (ImageView) convertView.findViewById(R.id.flavor_image);
        iconView.setImageResource(androidFlavor.image);

        TextView versionNameView = (TextView) convertView.findViewById(R.id.flavor_text);
        versionNameView.setText(androidFlavor.versionName
                + " - " + androidFlavor.versionNumber);

        return convertView;
    }
}
