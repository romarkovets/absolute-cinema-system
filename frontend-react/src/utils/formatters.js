export const formatDate = (dateArray) => {
  if (!dateArray) return '';
  const [year, month, day] = dateArray;
  return `${day.toString().padStart(2, '0')}.${month.toString().padStart(2, '0')}.${year}`;
};

export const formatTime = (timeArray) => {
  if (!timeArray) return '';
  const [hours, minutes] = timeArray;
  return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`;
};

export const formatDateTime = (dateArray, timeArray) => {
  return `${formatDate(dateArray)} ${formatTime(timeArray)}`;
};

export const formatPrice = (price) => {
  return new Intl.NumberFormat('ru-RU', {
    style: 'currency',
    currency: 'RUB',
    minimumFractionDigits: 0
  }).format(price);
};